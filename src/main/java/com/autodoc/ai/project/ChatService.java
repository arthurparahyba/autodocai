package com.autodoc.ai.project;

import com.autodoc.ai.prompt.QueryDocumentPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private QueryDocumentPrompt queryDocumentPrompt;


    public void process(String message, Long projectId){
        messageService.send(projectId, "Pergunta: "+message);
        var response = queryDocumentPrompt.execute(message, projectId);
        messageService.send(projectId, response);
    }


}
