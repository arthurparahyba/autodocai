package com.autodoc.ai.project.service;

import com.autodoc.ai.project.repository.ProjectEntity;
import com.autodoc.ai.shared.event.EventFileProcessingProducer;
import com.autodoc.ai.shared.event.LoadedFileContentEvent;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ProjectDocumentationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDocumentationService.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventFileProcessingProducer eventFileProcessingProducer;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Async
    public void generateDocs(ProjectEntity project) {
        final var projectPath = Path.of(project.getPath());

        final var folders = ProjectFileUtil.toProjectFolder(projectPath);
        var paths = folders.getFilesPath();
        eventFileProcessingProducer.setFilesPathEvent(project.getId(), paths);

        paths.stream().forEach(path -> {
            String fileText = ProjectFileUtil.readContentForFile(path);
            var fileContent = new ProjectFileUtil.ProjectFileContent(project.getId(), path, fileText);
            eventPublisher.publishEvent(new LoadedFileContentEvent(this, project.getId(), fileContent));
        });
    }


}
