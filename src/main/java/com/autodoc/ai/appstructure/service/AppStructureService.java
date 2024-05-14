package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.repository.Application;
import com.autodoc.ai.appstructure.repository.CodeEntity;
import com.autodoc.ai.appstructure.repository.Field;
import com.autodoc.ai.appstructure.repository.Function;
import com.autodoc.ai.appstructure.prompt.GenerateCodeEntityPrompt;
import com.autodoc.ai.appstructure.prompt.GetCodePathPrompt;
import com.autodoc.ai.appstructure.repository.AppStructureRepository;
import com.autodoc.ai.appstructure.repository.CodeEntityRepository;
import com.autodoc.ai.appstructure.repository.FunctionRepository;
import com.autodoc.ai.appstructure.to.*;
import com.autodoc.ai.project.service.ProjectService;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

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
    private GetCodePathPrompt getCodePathPrompt;

    @Autowired
    private GenerateCodeEntityPrompt generateCodeEntityPrompt;

    @Transactional
    public void process(Long appId, ProjectFileUtil.ProjectFile projectFile) {
        final var projectOp = projectService.findProjectById(appId);
        final var project = projectOp.orElseThrow(() ->new RuntimeException("Projeto %s não encontrado".formatted(appId)));
        final var projectName = project.getName();

        final var classInfoList = extractClassInfo(List.of(projectFile));

        final var entities = extractCodeEntitiesFrom(appId, classInfoList);
        codeEntityRepository.saveAll(entities);

        var app = appStructureRepository.merge(appId, projectName);
        app.setEntities(entities.stream().toList());
        classInfoList.stream().forEach(classInfo -> {
            saveFunctions(app, classInfo);
            saveFunctionsCalled(app, classInfo);
            updateAttributeType(app, classInfo);
        });

        appStructureRepository.save(app);
    }

    @Async
    public void process(Long appId, String appName, ProjectFileUtil.ProjectFolder folder) {
        var filesPath = folder.getFilesPath();
        filesPath = getCodePathPrompt.process(filesPath);

        Set<Path> filesPathSet = new HashSet<>(filesPath);
        final var projectFilesFound = folder.findWithPath(filesPathSet);
        final var classInfoList = extractClassInfo(projectFilesFound);

        final var entities = extractCodeEntitiesFrom(appId, classInfoList);
        codeEntityRepository.saveAll(entities);

        final Application app = new Application(appId, appName);
        app.setEntities(entities.stream().toList());

        classInfoList.stream().forEach(classInfo -> {
            saveFunctions(app, classInfo);
            saveFunctionsCalled(app, classInfo);
            updateAttributeType(app, classInfo);
        });

        appStructureRepository.save(app);
    }

    private List<ClassInfo> extractClassInfo(List<ProjectFileUtil.ProjectFile> projectFiles) {
        return projectFiles.stream()
            .map(projectFile -> {
                var classInfo = generateCodeEntityPrompt.process(projectFile.path(), projectFile.documentation().description());

                logger.info(classInfo.toString());
                return classInfo;
            }).toList();
    }

    private HashSet<CodeEntity> extractCodeEntitiesFrom(Long appId, List<ClassInfo> classInfoList) {
        final var entities = new HashSet<CodeEntity>();

        classInfoList.stream().forEach(classInfo -> {
            var mainEntity = toCodeEntity(appId, classInfo);
            entities.add(mainEntity);
        });

        classInfoList.stream().forEach(classInfo -> {
            var newEntities = createCodeEntity(appId, classInfo);
            var onlyNewEntities = newEntities.stream()
                .filter(newEntity -> !entities.contains(newEntity))
                .toList();
            entities.addAll(onlyNewEntities);
        });

        return entities;
    }

    private void updateAttributeType(Application app, ClassInfo classInfo) {
        var entity = app.findByFQN(classInfo.fqn());
        if(entity.isEmpty()) return;

        var entityFound = entity.get();

        entityFound.getFields().stream()
            .forEach(field -> {
                if(classInfo.attributes() == null) return;
                var attribute = classInfo.attributes().stream().filter(attr -> attr.name().equals(field.getName()))
                    .findFirst();
                if(attribute.isEmpty()) return;

                app.findByFQN(attribute.get().fqn())
                    .ifPresent(field::setType);
            });

//        this.codeEntityRepository.save(entityFound);
    }


    private void saveFunctionsCalled(Application app, ClassInfo classInfo) {
        var entity = app.findByFQN(classInfo.fqn());
        if(entity.isEmpty()) return;
        var entityFound = entity.get();

        classInfo.methods().stream()
                .forEach(method -> {
                    var function = entityFound.findFunctionByName(method.name());
                    if (function.isEmpty()) return;
                    var functionFound = function.get();

                    if(method.calls() == null) return;

                    method.calls().stream().forEach(call -> {
                        var calledEntity = app.findByFQN(call.fqn());
                        if(calledEntity.isEmpty()) return;

                        var calledEntityFound = calledEntity.get();

                        var calledFunction = calledEntityFound.getFunctions().stream()
                                .filter(f -> f.getName().equals(call.function()))
                                .findFirst();

                        if(calledFunction.isEmpty()) return;

                        functionFound.getFunctionsCalled().add(calledFunction.get());
//                        this.functionRepository.save(functionFound);

                    });
                });
    }


    private void saveFunctions(Application app, ClassInfo classInfo) {
        var entityFound = app.findByFQN(classInfo.fqn());
        if(entityFound.isEmpty()) {
            return;
        }
        var classInfoEntity = entityFound.get();

        classInfo.methods().stream().forEach(method -> {
            var function = new Function(app.getId(), method.name(), classInfoEntity);

            if(method.returns() != null) {
                app.findByFQN(method.returns().fqn())
                        .ifPresent(function::setReturnType);
            }

            if(method.params() != null) {
            method.params().stream().forEach(param -> {
                app.findByFQN(param.fqn())
                    .ifPresent(function::addParam);
            });
            }

            classInfoEntity.addFunction(function);

        });
//        this.codeEntityRepository.save(classInfoEntity);

    }

    private CodeEntity toCodeEntity(Long appId, ClassInfo classInfo) {
        var codeEntity = new CodeEntity(appId, classInfo.name(), classInfo.packageName());
        if(classInfo.attributes() != null) {
            final var fields = classInfo.attributes().stream()
                    .map(attr -> toField(attr))
                    .toList();
            codeEntity.setFields(fields);
        }

        return codeEntity;
    }

    private Set<CodeEntity> createCodeEntity(Long appId, ClassInfo classInfo) {
        var entities = new HashSet<CodeEntity>();

        if(classInfo.attributes() != null) {
            final var attrEntities = classInfo.attributes().stream()
                    .map(attr -> {
                    return new CodeEntity(appId, attr.className(), attr.packageName());
                })
                .filter(e -> !entities.contains(e))
                .toList();
            entities.addAll(attrEntities);
        }

        if(classInfo.dependencies() != null) {
            final var dependCodeEntities = classInfo.dependencies().stream()
                    .map(dep -> {
                        return new CodeEntity(appId, dep.className(), dep.packageName());
                    })
                    .filter(e -> !entities.contains(e))
                    .toList();
            entities.addAll(dependCodeEntities);
        }

        classInfo.methods().stream()
            .forEach(method -> {

                if(method.params() != null) {
                    final var paramCodeEntities = method.params().stream()
                            .map(param -> {
                                return new CodeEntity(appId, param.className(), param.packageName());
                            })
                            .filter(e -> !entities.contains(e))
                            .toList();
                    entities.addAll(paramCodeEntities);
                }

                if(method.calls() != null) {
                    final var calledCodeEntities = method.calls().stream()
                            .map(call -> {
                                return new CodeEntity(appId, call.className(), call.packageName());
                            })
                            .filter(e -> !entities.contains(e))
                            .toList();
                    entities.addAll(calledCodeEntities);
                }

                if(method.returns() != null) {
                    var returnedCodeEntity = new CodeEntity(appId, method.returns().className(), method.returns().packageName());
                    if(!entities.contains(returnedCodeEntity)){
                        entities.add(returnedCodeEntity);
                    }
                }

            });


        return entities;

    }

    private Field toField(Attribute attr) {
        return new Field(attr.name());

    }

}
