package com.autodoc.ai.project;

import com.autodoc.ai.project.util.ProjectFileUtil;
import com.autodoc.ai.prompt.GenerateFolderDocPrompt;
import com.autodoc.ai.prompt.GenerateFileDocPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectDocumentationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDocumentationService.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private GenerateFileDocPrompt filePrompt;

    @Autowired
    private GenerateFolderDocPrompt folderPrompt;

    @Autowired
    private VectorStore vectorStore;

    @Async
    public void generateDocs(ProjectEntity project) {
        final var projectPath = Path.of(project.getPath());

        ProjectFileUtil.ProjectFolder folder = ProjectFileUtil.toProjectFolder(projectPath);
        folder = generateDocumentationFor(folder, project.getId());

        createProjectStructureDocument(project, folder);
    }

    private ProjectFileUtil.ProjectFolder generateDocumentationFor(ProjectFileUtil.ProjectFolder folder, long projectId) {
        var documentedFolders = folder.folders().stream()
                .map(f -> generateDocumentationFor(f, projectId))
                .toList();

        var documentedFiles = folder.files().parallelStream()
                .map(file -> generateDocumentationFor(file, projectId))
                .toList();

        var documentation = folderPrompt.execute(folder.folderPath(), documentedFiles, documentedFolders);
        var doc = new Document(documentation, Map.of("projectId", projectId));
        vectorStore.add(List.of(doc));

        messageService.send(projectId, documentation);

        return new ProjectFileUtil.ProjectFolder(folder.projectPath(), folder.folderPath(), documentation, documentedFiles, documentedFolders);
    }

    private ProjectFileUtil.ProjectFile generateDocumentationFor(ProjectFileUtil.ProjectFile file, long projectId) {
        String fileContent = ProjectFileUtil.readContentForFile(file.path());

        var documentation = filePrompt.execute(file.path(), fileContent);
        var doc = new Document(documentation, Map.of("projectId", projectId));
        vectorStore.add(List.of(doc));

        messageService.send(projectId, documentation);

        return new ProjectFileUtil.ProjectFile(file.path(), documentation);
    }

    private void createProjectStructureDocument(ProjectEntity project, ProjectFileUtil.ProjectFolder folder) {
        String filesAndFolders =
                "A seguir a lista de arquivos do projeto (ID = %s): \n\n".formatted(project.getId());
        filesAndFolders +=  folder.getFilesPath()
                .stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.joining("\n"));
        filesAndFolders +=
                "\n\nA seguir a lista de pastas do projeto (ID = %s): \n\n".formatted(project.getId());
        filesAndFolders +=  folder.getFoldersPath()
                .stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.joining("\n"));

        var doc = new Document(filesAndFolders, Map.of("projectId", project.getId()));
        vectorStore.add(List.of(doc));
        messageService.send(project.getId(), "Documentação finalizada! Faça suas perguntas sobre o projeto e farei o melhor para respondê-las.");
    }
}
