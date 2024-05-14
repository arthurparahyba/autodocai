package com.autodoc.ai.appstructure.prompt;

import com.autodoc.ai.appstructure.to.ClassInfo;
import com.autodoc.ai.promptmanager.service.PromptSpecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class GenerateCodeEntityPrompt {

    private static final Logger logger = LoggerFactory.getLogger(GenerateCodeEntityPrompt.class);

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private PromptSpecService promptSpecService;

    public ClassInfo process(Path filePath, String fileDocumentation) {
        var promptSpec = promptSpecService.findByName("generate-code-entity").get();
        var prompt = promptSpec
                .setVariable("document", fileDocumentation)
                .setVariable("filePath", filePath.toString())
                .withOutputParser(ClassInfo.class)
                .withTemperature(0.60f)
                .build();

        Generation generation = chatClient.call(prompt).getResult();

        logger.info("---------------- CONVERSÃO: -----------------------");
        logger.info(generation.getOutput().getContent());
        ClassInfo classInfo = promptSpec.parse(generation.getOutput().getContent(), ClassInfo.class);
        return classInfo;
    }

}
