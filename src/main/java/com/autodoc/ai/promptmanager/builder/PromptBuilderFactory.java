package com.autodoc.ai.promptmanager.builder;

import com.autodoc.ai.promptmanager.model.ChainPromptBuilder;
import com.autodoc.ai.promptmanager.service.PromptSpecService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PromptBuilderFactory {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private PromptSpecService promptSpecService;

    public ChainPromptBuilder builder() {
        return new ChainPromptBuilder(chatClient, promptSpecService);
    }


}
