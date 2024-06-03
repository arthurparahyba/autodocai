package com.autodoc.ai.promptmanager.builder;

import com.autodoc.ai.promptmanager.model.PromptChainBuilder;
import com.autodoc.ai.promptmanager.service.PromptSpecService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderFactory {

    @Autowired
    private ObjectProvider<ChatClient> chatClient;

    @Autowired
    private PromptSpecService promptSpecService;

    public PromptChainBuilder builder() {
        return new PromptChainBuilder(chatClient, promptSpecService);
    }


}
