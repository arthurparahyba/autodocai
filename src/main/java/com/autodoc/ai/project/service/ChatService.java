package com.autodoc.ai.project.service;

import com.autodoc.ai.appstructure.service.GraphicQueryService;
import com.autodoc.ai.appsummary.service.SummaryChatService;
import com.autodoc.ai.project.to.UserIntent;
import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private SummaryChatService summaryChatService;

    @Autowired
    private GraphicQueryService graphicQueryService;

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;


    public void process(String message, Long projectId){
        var userIntent = promptBuilderFactory.builder()
            .toPrompt("identify-user-intent", "userMessage")
            .build(UserIntent.class)
            .execute(Map.of("userMessage", message));

        switch(userIntent.intent()){
            case DIAGRAM -> graphicQueryService.process(message, projectId);
            case TEXT -> summaryChatService.process(message, projectId);
        }
//        summaryChatService.process(message, projectId);
    }

}
