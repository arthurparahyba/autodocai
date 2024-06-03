package com.autodoc.ai.promptmanager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.parser.BeanOutputParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record AutodocPromptSpec(String name, String content, Map<String, Object> variables, List<String> functions, OpenAiChatOptions.Builder chatOptions) {

    private static final Logger logger = LoggerFactory.getLogger(AutodocPromptSpec.class);

    public AutodocPromptSpec setVariable(String name, Object value) {
        var tempVariables = new HashMap<>(variables);
        tempVariables.put(name, value);
        var clone = new AutodocPromptSpec(name, content, Map.copyOf(tempVariables), functions, chatOptions);
        return clone;
    }

    public AutodocPromptSpec setVariables(Map<String, String> newVariables) {
        var tempVariables = new HashMap<>(variables);
        tempVariables.putAll(newVariables);
        var clone = new AutodocPromptSpec(name, content, Map.copyOf(tempVariables), functions, chatOptions);
        return clone;
    }

    public AutodocPromptSpec withOutputParser(Class type) {
        final var outputParser = new BeanOutputParser<>(type);
        var tempVariables = new HashMap<>(variables);
        tempVariables.put("format", outputParser.getFormat());
        var clone = new AutodocPromptSpec(name, content, Map.copyOf(tempVariables), functions, chatOptions);
        return clone;
    }

    public AutodocPromptSpec withTemperature(Float temperature) {
        var chatOptionsBuilder = cloneChatOptions().withTemperature(temperature);
        return new AutodocPromptSpec(name, content, variables, functions, chatOptionsBuilder);
    }

    public Prompt build() {
        PromptTemplate promptTemplate = new PromptTemplate(this.content(), this.variables());
        Prompt systemPrompt = promptTemplate.create();

        var chatOptions = cloneChatOptions()
                .withModel("gpt-3.5-turbo")
                .withFrequencyPenalty(0f)
                .withPresencePenalty(0f);

        this.functions.stream().forEach(f -> chatOptions.withFunction(f));

        Message systemMessage = new SystemMessage(systemPrompt.getInstructions().get(0).getContent());

        ChatOptions options = chatOptions.build();
        Prompt prompt = new Prompt(
                List.of(systemMessage),
                options
        );

        return prompt;
    }

    public <T> T parse(String content, Class<T> type){
        final var outputParser = new BeanOutputParser(type);
        var parsedObject = outputParser.parse(content);
        return (T) parsedObject;
    }

    private OpenAiChatOptions.Builder cloneChatOptions() {
        var chatOptions = this.chatOptions.build();
        return OpenAiChatOptions.builder()
                .withTemperature(chatOptions.getTemperature())
                .withFrequencyPenalty(chatOptions.getFrequencyPenalty())
                .withPresencePenalty(chatOptions.getPresencePenalty())
                .withModel(chatOptions.getModel());
    }
}
