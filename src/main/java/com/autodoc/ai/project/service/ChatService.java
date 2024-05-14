package com.autodoc.ai.project.service;

import com.autodoc.ai.appsummary.service.SummaryChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private SummaryChatService summaryChatService;


    public void process(String message, Long projectId){
        summaryChatService.process(message, projectId);
    }

}
