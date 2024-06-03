package com.autodoc.ai.promptmanager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromptChain<R> {

    private static final Logger logger = LoggerFactory.getLogger(PromptChain.class);

    private final List<IPromptEntity> prompts;

    private final ObjectProvider<ChatClient> chatClient;

    private final Class<R> returnType;

    public PromptChain(List<IPromptEntity> prompts, ObjectProvider<ChatClient> chatClient, Class<R> returnType) {
        this.prompts = prompts;
        this.chatClient = chatClient;
        this.returnType = returnType;
    }

    public R execute(Map<String, String> variables) {
        String lastResult = null;
        for(var promptEntity : prompts) {
            switch(promptEntity) {
                case PromptEntity p -> {
                    var promptSpec = p.getPromptSpec();
                    if(p.getVariableNames() != null) {
                        var variablesMap = p.getVariableNames().stream().collect(Collectors.toMap(
                                key -> key,
                                key -> variables.get(key)
                        ));
                        promptSpec = promptSpec.setVariables(variablesMap);
                    }
                    if(lastResult != null) {
                        promptSpec = promptSpec.setVariable("beforeOutput", lastResult);
                    }
                    if(promptEntity == prompts.getLast()) {
                        promptSpec = promptSpec.withOutputParser(returnType);
                    }
                    try {
                        lastResult = call(promptSpec);
                    } catch(Exception e) {
                        logger.error(STR."Erro ao processar o prompt \{promptSpec.name()}", e);
                    }
                }

                case PromptEntityValidator p -> {
                    var promptSpec = p.getPromptSpec();
                    promptSpec = promptSpec.setVariable("beforeOutput", lastResult);
                    promptSpec = promptSpec.withOutputParser(PromptEntityValidatorOutput.class);
                    var validationResult = call(promptSpec);
                    var validatorOutput = parse(validationResult, PromptEntityValidatorOutput.class);
                    if(validatorOutput.equals(PromptEntityValidatorOutput.IS_VALID)){
                        continue;
                    } else {
                        return (R) p.getDefaultResponse();
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

    private String call(AutodocPromptSpec promptSpec) {
        Generation generation = chatClient.getObject().call(promptSpec.build()).getResult();
        return generation.getOutput().getContent();
    }

    private <T> T parse(String content, Class<T> type){
        final var outputParser = new BeanOutputParser(type);
        var parsedObject = outputParser.parse(content);
        return (T) parsedObject;
    }
}
