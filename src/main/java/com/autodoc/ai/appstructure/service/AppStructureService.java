package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.repository.*;
import com.autodoc.ai.appstructure.repository.Module;
import com.autodoc.ai.appstructure.to.*;
import com.autodoc.ai.appstructure.to.ApplicationByDocument;
import com.autodoc.ai.appstructure.to.ModuleList;
import com.autodoc.ai.project.service.ProjectService;
import com.autodoc.ai.promptmanager.model.ParallelPromptChain;
import com.autodoc.ai.promptmanager.model.PromptChain;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Qualifier("parallelPromptChainCodeEntity")
    private ParallelPromptChain parallelPromptChainForCodeEntity;

    @Autowired
    private PromptChain<ModuleList> moduleListPromptChain;

    @Transactional
    public void process(Long appId) {
        var entities = codeEntityRepository.findCodeEntitiesByApplicationId(appId);
        var classesFQNAsText = entities.stream().map(e -> STR."ClassName: \{e.getClassName()}, PackageName: \{e.getPackageName()}").collect(Collectors.joining("\n"));

        var moduleList = moduleListPromptChain
                .execute(Map.of("classes", classesFQNAsText));

        moduleList.modules().forEach(module -> {
            var newModule = new Module(appId, module.moduleName());
            final var application = appStructureRepository.merge(appId);
            application.addModule(newModule);
            appStructureRepository.save(application);

            module.classes().forEach(moduleClass -> {
                var moduleCodeEntity = new CodeEntity(appId, moduleClass.className(), moduleClass.packageName());
                moduleCodeEntity.setModule(newModule);
                codeEntityRepository.save(moduleCodeEntity);
            });
        });
    }


    @Transactional
    public void createApplication(ApplicationByDocument app) {
        final var projectOp = projectService.findProjectById(app.appId());
        final var project = projectOp.orElseThrow(() ->new RuntimeException("Projeto %s não encontrado".formatted(app.appId())));
        final var projectName = project.getName();
        final var application = appStructureRepository.merge(app.appId(), projectName);
        application.addLayer(app.layer());
        application.setEntities(app.entities());
        appStructureRepository.save(application);
    }

    @Transactional
    public ApplicationByDocument process(Long appId, ProjectFileUtil.ProjectFileContent fileContent) {
        final var results = parallelPromptChainForCodeEntity.execute(Map.of("code", fileContent.content()));
        ClassInfo classInfo = results.get(ClassInfo.class);
        MethodParameters methods = results.get(MethodParameters.class);
        MethodCallsList methodCalls = results.get(MethodCallsList.class);
        ClassLayerInfo classLayer = results.get(ClassLayerInfo.class);
        MethodParameterCallsList methodParameterCalls = results.get(MethodParameterCallsList.class);

        final var entities = new HashSet<CodeEntity>();
        final Layer layer = new Layer(appId, classLayer.layer().name());
        final var mainCodeEntity = new CodeEntity(appId, classInfo.nomeClasse(), classInfo.nomePacote(), layer);

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

        methodParameterCalls.methodParameterCalls().stream()
            .forEach(parameterCalls -> {
                var functionsCalled = parameterCalls.parameterMethodCalls().stream()
                    .map(parameterCall -> {
                        var entityParameterFunction = new CodeEntity(appId, parameterCall.className(), parameterCall.packageName());
                        return new Function(appId, parameterCall.methodName(), entityParameterFunction);
                    }).toList();
                functions.stream().filter(f-> f.getName().equalsIgnoreCase(parameterCalls.methodName())).findFirst()
                        .ifPresent(f -> f.setFunctionsCalled(functionsCalled));
            });

        entities.add(mainCodeEntity);

        final List<Field> fields = createFields(appId, classInfo);
        mainCodeEntity.setFields(fields);
        return new ApplicationByDocument(appId, layer, entities.stream().toList());

    }

    private List<Field> createFields(Long appId,ClassInfo classInfo) {
        return classInfo.attributes().stream()
                .map(attr -> {
                    var fieldEntity = new CodeEntity(appId, attr.nomeClasse(), attr.nomePacote());
                    return new Field(attr.nomeAtributo(), fieldEntity);
                }).toList();
    }

}
