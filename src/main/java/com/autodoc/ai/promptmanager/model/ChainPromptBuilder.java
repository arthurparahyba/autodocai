package com.autodoc.ai.promptmanager.model;

import com.autodoc.ai.promptmanager.service.PromptSpecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.parser.BeanOutputParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ChainPromptBuilder{
    private static final Logger logger = LoggerFactory.getLogger(ChainPromptBuilder.class);
    public static final float DEFAULT_TEMPERATURE = 0.60f;
    private List<IPromptEntity> prompts = new ArrayList<>();
    private ChatClient chatClient;
    private PromptSpecService promptSpecService;

    public ChainPromptBuilder(ChatClient chatClient, PromptSpecService promptSpecService) {
        this.chatClient = chatClient;
        this.promptSpecService = promptSpecService;
    }

    public ChainPromptBuilder toPrompt(String promptName, Map<String, String> variables) {
        var newPrompt = new PromptEntity(promptName, variables);
        prompts.add(newPrompt);
        return this;
    }

    public ChainPromptBuilder toPrompt(String promptName) {
        var newPrompt = new PromptEntity(promptName, Map.of());
        prompts.add(newPrompt);
        return this;
    }

    public ChainPromptBuilder toPromptValidator(String promptName, Object defaultResponse) {
        var newPrompt = new PromptEntityValidator(promptName, defaultResponse);
        prompts.add(newPrompt);
        return this;
    }

    public <R> R build(Class<R> returnType) {
        String lastResult = null;
        for(var promptEntity : prompts) {
            switch(promptEntity) {
                case PromptEntity p -> {
                    var promptSpec = getPromptSpec(p);
                    if(lastResult != null) {
                        promptSpec.setVariable("beforeOutput", lastResult);
                    }
                    if(promptEntity == prompts.getLast()) {
                        promptSpec.withOutputParser(returnType);
                    }
                    lastResult = call(promptSpec);
                }

                case PromptEntityValidator p -> {
                    var promptSpec = getPromptSpec(p);
                    promptSpec.setVariable("beforeOutput", lastResult);
                    promptSpec.withOutputParser(PromptEntityValidatorOutput.class);
                    var validationResult = call(promptSpec);
                    var validatorOutput = parse(validationResult, PromptEntityValidatorOutput.class);
                    if(validatorOutput.equals(PromptEntityValidatorOutput.IS_VALID)){
                        continue;
                    } else {
                        return (R) p.defaultResponse();
                    }
                }
            }

        }
        try {
            if(returnType.equals(String.class)) {
                return (R) lastResult;
            }
            return parse(lastResult, returnType);
        }catch(Exception e) {
            logger.error("Erro ao executar o parse de %s para o tipo %s".formatted(lastResult, returnType), e);
            throw new RuntimeException("Erro ao executar o parse", e);
        }
    }

    private AutodocPromptSpec getPromptSpec(PromptEntity promptEntity) {
        var promptSpec = promptSpecService.findByName(promptEntity.name()).get();

        promptEntity.variables().entrySet().stream().forEach(variable -> {
            promptSpec.setVariable(variable.getKey(), variable.getValue());
        });

        return promptSpec
                .withTemperature(DEFAULT_TEMPERATURE);
    }

    private AutodocPromptSpec getPromptSpec(PromptEntityValidator promptEntity) {
        var promptSpec = promptSpecService.findByName(promptEntity.name()).get();
        return promptSpec
                .withTemperature(DEFAULT_TEMPERATURE);
    }

    private String call(AutodocPromptSpec promptSpec) {
        Generation generation = chatClient.call(promptSpec.build()).getResult();
        return generation.getOutput().getContent();
    }

    private <T> T parse(String content, Class<T> type){
        final var outputParser = new BeanOutputParser(type);
        var parsedObject = outputParser.parse(content);
        return (T) parsedObject;
    }

}
