package com.autodoc.ai.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public static final String TOPIC_PROJECT_STATUS = "/topic/projectStatus/";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(long projectId, String message) {
        messagingTemplate.convertAndSend(TOPIC_PROJECT_STATUS + projectId, message);
    }
}
