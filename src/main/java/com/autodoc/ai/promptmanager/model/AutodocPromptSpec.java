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

import java.util.List;
import java.util.Map;

public record AutodocPromptSpec(String name, String content, Map<String, Object> variables, List<String> functions, List<Message> messages, OpenAiChatOptions.Builder chatOptions) {

    private static final Logger logger = LoggerFactory.getLogger(AutodocPromptSpec.class);

    public AutodocPromptSpec setVariable(String name, Object value) {
        this.variables.put(name, value);
        return this;
    }

    public AutodocPromptSpec withUserMessage(String userMessage) {
        messages.add(new UserMessage(userMessage));
        return this;
    }

    public AutodocPromptSpec withOutputParser(Class type) {
        final var outputParser = new BeanOutputParser<>(type);
        variables.put("format", outputParser.getFormat());
        return this;
    }

    public AutodocPromptSpec withTemperature(Float temperature) {
        this.chatOptions.withTemperature(temperature);
        return this;
    }

    public Prompt build() {
        PromptTemplate promptTemplate = new PromptTemplate(this.content(), this.variables());
        Prompt systemPrompt = promptTemplate.create();

        chatOptions
                .withModel("gpt-3.5-turbo")
                .withFrequencyPenalty(0f)
                .withPresencePenalty(0f);

        this.functions.stream().forEach(f -> chatOptions.withFunction(f));

        Message systemMessage = new SystemMessage(systemPrompt.getInstructions().get(0).getContent());
        messages.add(systemMessage);

        ChatOptions options = chatOptions.build();
        Prompt prompt = new Prompt(
                messages,
                options
        );

        return prompt;
    }

    public <T> T parse(String content, Class<T> type){
        final var outputParser = new BeanOutputParser(type);
        var parsedObject = outputParser.parse(content);
        return (T) parsedObject;
    }
}
