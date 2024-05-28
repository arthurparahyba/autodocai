package com.autodoc.ai.appstructure.prompt;

import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.promptmanager.service.PromptSpecService;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GenerateDiagramPrompt {

    private static final Logger logger = LoggerFactory.getLogger(GenerateDiagramPrompt.class);

    @Autowired
    private OpenAiChatClient chatClient;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PromptSpecService promptSpecService;

    @Async
    public void process(String userMessage, Long applicationId, MessageService.TagMessage tagMessage) {
        var promptSpec = promptSpecService.findByName("generate-diagram-query").get();
        var prompt = promptSpec
                .setVariable("applicationId", applicationId)
                .withOutputParser(MermaidResponse.class)
                .withUserMessage(userMessage)
                .withTemperature(0.40f)
                .build();

        var clientResp = chatClient.call(prompt).getResult().getOutput().getContent();
        MermaidResponse response = promptSpec.parse(clientResp, MermaidResponse.class);
        messageService.send(applicationId, tagMessage.toGraphicMessage(response.mermaidText));
    }

    @JsonClassDescription("Objeto representa a resposta a ser enviada para o usuário com o código mermaid")
    public record MermaidResponse(@JsonPropertyDescription("Código mermaid") String mermaidText) {};

}
