package com.autodoc.ai.promptmanager.model;

import com.autodoc.ai.promptmanager.service.PromptSpecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromptChainBuilder {
    private static final Logger logger = LoggerFactory.getLogger(PromptChainBuilder.class);
    public static final float DEFAULT_TEMPERATURE = 0.60f;
    private final List<IPromptEntity> prompts = new ArrayList<>();
    private final ObjectProvider<ChatClient> chatClient;
    private final PromptSpecService promptSpecService;

    public PromptChainBuilder(ObjectProvider<ChatClient> chatClient, PromptSpecService promptSpecService) {
        this.chatClient = chatClient;
        this.promptSpecService = promptSpecService;
    }

    public PromptChainBuilder toPrompt(String promptName, String... variables) {
        var promptSpec = promptSpecService.findByName(promptName).get();
        promptSpec.withTemperature(DEFAULT_TEMPERATURE);

        var newPrompt = new PromptEntity(promptSpec, variables);
        prompts.add(newPrompt);
        return this;
    }

    public PromptChainBuilder toPrompt(String promptName) {
        var promptSpec = promptSpecService.findByName(promptName).get();
        promptSpec.withTemperature(DEFAULT_TEMPERATURE);
        var newPrompt = new PromptEntity(promptSpec);
        prompts.add(newPrompt);
        return this;
    }

    public PromptChainBuilder toPromptValidator(String promptName, Object defaultResponse) {
        var promptSpec = promptSpecService.findByName(promptName).get();
        promptSpec.withTemperature(DEFAULT_TEMPERATURE);
        var newPrompt = new PromptEntityValidator(promptSpec, defaultResponse);
        prompts.add(newPrompt);
        return this;
    }

    public <R> PromptChain<R> build(Class<R> returnType) {
        return new PromptChain<R>(this.prompts, this.chatClient, returnType);
    }

}
