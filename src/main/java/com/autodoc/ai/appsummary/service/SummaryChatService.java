package com.autodoc.ai.appsummary.service;

import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.appsummary.prompt.QueryDocumentPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryChatService {

    private static final Logger logger = LoggerFactory.getLogger(SummaryChatService.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private QueryDocumentPrompt queryDocumentPrompt;

    public void process(String message, Long projectId){
        var tag = MessageService.createTag();
        messageService.send(projectId, tag);
        queryDocumentPrompt.execute(message, projectId, tag);
    }
}
