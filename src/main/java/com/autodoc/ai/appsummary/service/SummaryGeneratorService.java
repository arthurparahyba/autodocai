package com.autodoc.ai.appsummary.service;

import com.autodoc.ai.appsummary.prompt.FilePromptProcessor;
import com.autodoc.ai.appsummary.prompt.GenerateFileCodePrompt;
import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.shared.event.CreatedFileDocEvent;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import com.autodoc.ai.appsummary.prompt.GenerateFileGenericDocPrompt;
//import com.autodoc.ai.appsummary.prompt.GenerateFolderDocPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(SummaryGeneratorService.class);

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private MessageService messageService;

    @Autowired
    private List<FilePromptProcessor> filePromptList;

//    @Autowired
//    private GenerateFolderDocPrompt folderPrompt;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void process(ProjectFileUtil.ProjectFileContent content) {
        var fileDoc = generateDocumentationFor(content);
        var doc = new Document(fileDoc.documentation().description(), Map.of("projectId", content.projectId()));
        vectorStore.add(List.of(doc));
    }

//    public ProjectFileUtil.ProjectFolder process(ProjectFileUtil.ProjectFolder projectFolder, Long projectId, MessageService.TagMessage tag) {
//        projectFolder = generateDocumentationFor(projectFolder, projectId, tag);
//        createProjectStructureDocument(projectFolder, projectId);
//        messageService.send(projectId, tag.toTextMessage("Documentação finalizada! Faça suas perguntas sobre o projeto e farei o melhor para respondê-las. [END]"));
//        return projectFolder;
//    }
//
//    private ProjectFileUtil.ProjectFolder generateDocumentationFor(ProjectFileUtil.ProjectFolder folder, long projectId, MessageService.TagMessage tag) {
//        var folders = folder.folders().parallelStream()
//                .map(f -> generateDocumentationFor(f, projectId, tag))
//                .toList();
//
//        var documentedFiles = folder.files().parallelStream()
//                .map(file -> {
//                    var fileDoc = generateDocumentationFor(file, projectId, tag);
//                    eventPublisher.publishEvent(new CreatedFileDocEvent(this, projectId, fileDoc));
//                    return fileDoc;
//                })
//                .toList();
//
//        return new ProjectFileUtil.ProjectFolder(folder.projectPath(), folder.folderPath(), documentedFiles, folders);
//    }

    private ProjectFileUtil.ProjectFile generateDocumentationFor(ProjectFileUtil.ProjectFileContent fileContent) {
        var content = fileContent.content();
        var filePrompt = findPromptProcessor(fileContent.path());
        var responseOp = filePrompt.execute(fileContent.path(), content);
        if(responseOp.isEmpty()) {
            logger.warn("Não foi possível gerar a documentação do arquivo %s".formatted(fileContent.path()));
            var fileDoc = new ProjectFileUtil.ProjectFileDoc("", "");
            return new ProjectFileUtil.ProjectFile(fileContent.path(), fileDoc);
        }

        var response = responseOp.get();
        return switch(response) {
            case GenerateFileCodePrompt.Response res -> {
                var fileDoc = new ProjectFileUtil.ProjectFileCodeDoc(res.getResume(), res.getDocumentation());
                yield new ProjectFileUtil.ProjectFile(fileContent.path(), fileDoc);
            }
            case GenerateFileGenericDocPrompt.Response res -> {
                var fileDoc = new ProjectFileUtil.ProjectFileDoc(res.getResume(), res.getDocumentation());
                yield new ProjectFileUtil.ProjectFile(fileContent.path(), fileDoc);
            }
        };
    }

//    private ProjectFileUtil.ProjectFile generateDocumentationFor(ProjectFileUtil.ProjectFile file, long projectId, MessageService.TagMessage tag) {
//        String fileContent = ProjectFileUtil.readContentForFile(file.path());
//
//        var filePrompt = findPromptProcessor(file.path());
//        var responseOp = filePrompt.execute(file.path(), fileContent);
//        if(responseOp.isEmpty()) {
//            logger.warn("Não foi possível gerar a documentação do arquivo %s".formatted(file.path()));
//            var fileDoc = new ProjectFileUtil.ProjectFileDoc("", "");
//            return new ProjectFileUtil.ProjectFile(file.path(), fileDoc);
//        }
//
//        var response = responseOp.get();
//        var doc = new Document(response.getDocumentation(), Map.of("projectId", projectId));
//        vectorStore.add(List.of(doc));
//        messageService.send(projectId, tag.toTextMessage(response.getResume()));
//
//        return switch(response) {
//            case GenerateFileCodePrompt.Response res -> {
//                var fileDoc = new ProjectFileUtil.ProjectFileCodeDoc(res.getResume(), res.getDocumentation());
//                yield new ProjectFileUtil.ProjectFile(file.path(), fileDoc);
//            }
//            case GenerateFileGenericDocPrompt.Response res -> {
//                var fileDoc = new ProjectFileUtil.ProjectFileDoc(res.getResume(), res.getDocumentation());
//                yield new ProjectFileUtil.ProjectFile(file.path(), fileDoc);
//            }
//        };
//    }

    private FilePromptProcessor findPromptProcessor(Path path) {
       var prompt = this.filePromptList.stream().filter(p -> p.accept(path)).findFirst();
       if(prompt.isEmpty()) {
           throw new RuntimeException("FilePromptProcessor not found for file $s".formatted(path.toString()));
       }
       return prompt.get();
    }

//    private void createProjectStructureDocument(ProjectFileUtil.ProjectFolder folder, Long projectId) {
//        String filesAndFolders =
//                "A seguir a lista de arquivos do projeto (ID = %s): \n\n".formatted(projectId);
//        filesAndFolders +=  folder.getFilesPath()
//                .stream()
//                .map(Path::toAbsolutePath)
//                .map(Path::toString)
//                .collect(Collectors.joining("\n"));
//        filesAndFolders +=
//                "\n\nA seguir a lista de pastas do projeto (ID = %s): \n\n".formatted(projectId);
//        filesAndFolders +=  folder.getFoldersPath()
//                .stream()
//                .map(Path::toAbsolutePath)
//                .map(Path::toString)
//                .collect(Collectors.joining("\n"));
//
//        var doc = new Document(filesAndFolders, Map.of("projectId", projectId));
//        vectorStore.add(List.of(doc));
//    }
}
