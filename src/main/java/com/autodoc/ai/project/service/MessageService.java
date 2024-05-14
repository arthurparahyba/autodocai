package com.autodoc.ai.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class MessageService {
    public static final String TOPIC_PROJECT_STATUS = "/topic/projectStatus/";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(long projectId, ContentMessage message) {
        messagingTemplate.convertAndSend(TOPIC_PROJECT_STATUS + projectId, message.content, Map.of("type", message.type, "tagId", message.tagId));
    }

    public void send(long projectId, TagMessage message) {
        messagingTemplate.convertAndSend(TOPIC_PROJECT_STATUS + projectId, "", Map.of("type", message.type, "tagId", message.tagId));
    }

    public enum MessageType {
        DOCUMENT, GRAPHIC, CREATE_TAG
    }

    public record ContentMessage(MessageType type, String tagId, String content) {}

    public record TagMessage(MessageType type, String tagId) {
        public ContentMessage toTextMessage(String content) {
            if(content == null) content = "";
            return new ContentMessage(MessageType.DOCUMENT, this.tagId(), content);
        }

        public ContentMessage toGraphicMessage(String content) {
            return new ContentMessage(MessageType.GRAPHIC, this.tagId(), content);
        }

    }

    public static TagMessage createTag() {
        var tagId = "X"+ UUID.randomUUID().toString();
        return new TagMessage(MessageType.CREATE_TAG, tagId);
    }

}
