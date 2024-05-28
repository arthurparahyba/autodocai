//package com.autodoc.ai.appstructure.prompt;
//
//import com.autodoc.ai.appstructure.to.ClassInfo;
//import com.autodoc.ai.promptmanager.service.PromptSpecService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.ChatClient;
//import org.springframework.ai.chat.Generation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.Path;
//
//@Component
//public class GenerateClassAndAttributesDescriptionPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(GenerateClassAndAttributesDescriptionPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    @Autowired
//    private PromptSpecService promptSpecService;
//
//    public String process(String fileContent) {
//        var promptSpec = promptSpecService.findByName("generate-class-and-attributes-description").get();
//        var prompt = promptSpec
//                .setVariable("code", fileContent)
//                .withOutputParser(ClassInfo.class)
//                .withTemperature(0.60f)
//                .build();
//
//        Generation generation = chatClient.call(prompt).getResult();
//        return generation.getOutput().getContent();
//
//    }
//
//}
