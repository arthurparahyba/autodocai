package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.prompt.*;
import com.autodoc.ai.appstructure.repository.*;
import com.autodoc.ai.appstructure.to.*;
import com.autodoc.ai.project.service.ProjectService;
import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;

@Service
public class AppStructureService {

    private static final Logger logger = LoggerFactory.getLogger(AppStructureService.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AppStructureRepository appStructureRepository;

    @Autowired
    private CodeEntityRepository codeEntityRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;


    @Transactional
    public void process(Long appId, ProjectFileUtil.ProjectFileContent fileContent) {
        final var projectOp = projectService.findProjectById(appId);
        final var project = projectOp.orElseThrow(() ->new RuntimeException("Projeto %s não encontrado".formatted(appId)));
        final var projectName = project.getName();

        List<Callable<Object>> tasks = Arrays.asList(
                () -> generateClassAndAttributeDescriptionBy(fileContent),
                () -> generateMethodInfoBy(fileContent),
                () -> generateMethodCallsFromAttributes(fileContent),
                () -> generateClassLayerInfoBy(fileContent)
        );
        List<Object> results = tasks.parallelStream()
                .map(task -> {
                    try {
                        return task.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        ClassInfo classInfo = (ClassInfo) results.get(0);
        MethodParameters methods = (MethodParameters) results.get(1);
        MethodCallsList methodCalls = (MethodCallsList) results.get(2);
        ClassLayerInfo classLayer = (ClassLayerInfo) results.get(3);

        var entities = new HashSet<CodeEntity>();
        final Application app = new Application(appId, projectName);

        final Layer layer = new Layer(classLayer.layer().name());
        var mainCodeEntity = new CodeEntity(appId, classInfo.nomeClasse(), classInfo.nomePacote(), layer);

        var functions = methods.metodos().stream()
            .map(method -> {
                var entityParameters = method.parametros().stream().map(param -> new CodeEntity(appId, param.nomeClasse(), param.nomePacote()))
                        .toList();
                if(method.retorno() != null) {
                    var returnType = new CodeEntity(appId, method.retorno().nomeClasse(), method.retorno().nomePacote());
                    return new Function(appId, method.nome(), mainCodeEntity, entityParameters, returnType);
                }
                return new Function(appId, method.nome(), mainCodeEntity, entityParameters);
            }).toList();
        mainCodeEntity.setFunctions(functions);

        methodCalls.metodos().stream()
            .forEach(metodo -> {
                var functionsCalled = metodo.chamadas().stream()
                    .map(call -> {
                        var entityFunction = new CodeEntity(appId, call.nomeClasse(), call.nomePacote());
                        return new Function(appId, call.nomeMetodoChamado(), entityFunction);
                    }).toList();

                functions.stream().filter(f-> f.getName().equalsIgnoreCase(metodo.nomeMetodo())).findFirst()
                        .ifPresent(f -> f.setFunctionsCalled(functionsCalled));
            });

        entities.add(mainCodeEntity);

        final List<Field> fields = createFields(appId, classInfo);
        mainCodeEntity.setFields(fields);
        app.addLayer(layer);
        app.setEntities(entities.stream().toList());
        appStructureRepository.save(app);

    }

    private MethodCallsList generateMethodCallsFromAttributes(ProjectFileUtil.ProjectFileContent fileContent) {
        var defaultResponse = new MethodCallsList(new ArrayList<>());

        return promptBuilderFactory.builder()
                .toPrompt("extract-attributes-from-code", Map.of("code", fileContent.content()))
                .toPromptValidator("extract-attributes-from-code-validation", defaultResponse)
                .toPrompt("select-application-attributes")
                .toPrompt("format-list-of-application-attributes")
                .toPromptValidator("extract-attributes-from-code-validation", defaultResponse)
                .toPrompt("list-methods-and-called-attributes-methods", Map.of("code", fileContent.content()))
                .build(MethodCallsList.class);
    }

    private ClassInfo generateClassAndAttributeDescriptionBy(ProjectFileUtil.ProjectFileContent fileContent) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-class-and-attributes-description", Map.of("code", fileContent.content()))
                .toPrompt("convert-classinfo-description-to-json")
                .build(ClassInfo.class);
    }

    private MethodParameters generateMethodInfoBy(ProjectFileUtil.ProjectFileContent fileContent) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-methods-description", Map.of("code", fileContent.content()))
                .toPrompt("convert-method-description-to-json")
                .build(MethodParameters.class);
    }

    private ClassLayerInfo generateClassLayerInfoBy(ProjectFileUtil.ProjectFileContent fileContent) {
        return promptBuilderFactory.builder()
                .toPrompt("extract-application-layer-category", Map.of("code", fileContent.content()))
                .build(ClassLayerInfo.class);
    }

    private List<Field> createFields(Long appId,ClassInfo classInfo) {
        return classInfo.attributes().stream()
                .map(attr -> {
                    var fieldEntity = new CodeEntity(appId, attr.nomeClasse(), attr.nomePacote());
                    return new Field(attr.nomeAtributo(), fieldEntity);
                }).toList();
    }

}
