package com.autodoc.ai.appsummary.service;

import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.shared.event.CreatedFileDocEvent;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import com.autodoc.ai.appsummary.prompt.GenerateFileDocPrompt;
import com.autodoc.ai.appsummary.prompt.GenerateFolderDocPrompt;
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

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GenerateFileDocPrompt filePrompt;

    @Autowired
    private GenerateFolderDocPrompt folderPrompt;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ProjectFileUtil.ProjectFolder process(ProjectFileUtil.ProjectFolder projectFolder, Long projectId, MessageService.TagMessage tag) {
        projectFolder = generateDocumentationFor(projectFolder, projectId, tag);
        createProjectStructureDocument(projectFolder, projectId);
        messageService.send(projectId, tag.toTextMessage("Documentação finalizada! Faça suas perguntas sobre o projeto e farei o melhor para respondê-las. [END]"));
        return projectFolder;
    }

    private ProjectFileUtil.ProjectFolder generateDocumentationFor(ProjectFileUtil.ProjectFolder folder, long projectId, MessageService.TagMessage tag) {
        var documentedFolders = folder.folders().parallelStream()
                .map(f -> generateDocumentationFor(f, projectId, tag))
                .toList();

        var documentedFiles = folder.files().parallelStream()
                .map(file -> {
                    var fileDoc = generateDocumentationFor(file, projectId, tag);
                    eventPublisher.publishEvent(new CreatedFileDocEvent(this, projectId, fileDoc));
                    return fileDoc;
                })
                .toList();

        var documentation = folderPrompt.execute(folder.folderPath(), documentedFiles, documentedFolders);
        var doc = new Document(documentation, Map.of("projectId", projectId));
        vectorStore.add(List.of(doc));

        messageService.send(projectId, tag.toTextMessage(documentation));

        return new ProjectFileUtil.ProjectFolder(folder.projectPath(), folder.folderPath(), documentation, documentedFiles, documentedFolders);
    }

    private ProjectFileUtil.ProjectFile generateDocumentationFor(ProjectFileUtil.ProjectFile file, long projectId, MessageService.TagMessage tag) {
        String fileContent = ProjectFileUtil.readContentForFile(file.path());

        var response = filePrompt.execute(file.path(), fileContent);
        var doc = new Document(response.getDocumentation(), Map.of("projectId", projectId));
        vectorStore.add(List.of(doc));

        messageService.send(projectId, tag.toTextMessage(response.getResume()));

        var fileDoc = new ProjectFileUtil.ProjectFileDoc(response.getResume(), response.getDocumentation(), response.documentationCategory());
        return new ProjectFileUtil.ProjectFile(file.path(), fileDoc);
    }

    private void createProjectStructureDocument(ProjectFileUtil.ProjectFolder folder, Long projectId) {
        String filesAndFolders =
                "A seguir a lista de arquivos do projeto (ID = %s): \n\n".formatted(projectId);
        filesAndFolders +=  folder.getFilesPath()
                .stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.joining("\n"));
        filesAndFolders +=
                "\n\nA seguir a lista de pastas do projeto (ID = %s): \n\n".formatted(projectId);
        filesAndFolders +=  folder.getFoldersPath()
                .stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.joining("\n"));

        var doc = new Document(filesAndFolders, Map.of("projectId", projectId));
        vectorStore.add(List.of(doc));
    }
}
