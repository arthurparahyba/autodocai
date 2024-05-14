package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.prompt.GenerateDiagramPrompt;
import com.autodoc.ai.project.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphicQueryService {

    private static final Logger logger = LoggerFactory.getLogger(GraphicQueryService.class);

    @Autowired
    private GenerateDiagramPrompt generateDiagramPrompt;

    @Autowired
    private MessageService messageService;

    public void generate(String userMessage, Long applicationId) {
        var tagMessage = MessageService.createTag();
        messageService.send(applicationId, tagMessage);
        generateDiagramPrompt.process(userMessage, applicationId, tagMessage);

    }
}
