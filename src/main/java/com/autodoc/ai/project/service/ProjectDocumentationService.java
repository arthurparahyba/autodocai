package com.autodoc.ai.project.service;

import com.autodoc.ai.appstructure.service.AppStructureService;
import com.autodoc.ai.appsummary.service.SummaryGeneratorService;
import com.autodoc.ai.project.repository.ProjectEntity;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ProjectDocumentationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDocumentationService.class);

    @Autowired
    private SummaryGeneratorService summaryService;

    @Autowired
    private MessageService messageService;

    @Async
    public void generateDocs(ProjectEntity project) {
        final var projectPath = Path.of(project.getPath());

        final var folders = ProjectFileUtil.toProjectFolder(projectPath);

        var tag = MessageService.createTag();
        final var documentedFolders = summaryService.process(folders, project.getId(), tag);

    }


}
