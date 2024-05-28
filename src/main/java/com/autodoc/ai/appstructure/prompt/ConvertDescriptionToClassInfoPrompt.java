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
//@Component
//public class ConvertDescriptionToClassInfoPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(ConvertDescriptionToClassInfoPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    @Autowired
//    private PromptSpecService promptSpecService;
//
//    public ClassInfo process(String description) {
//        var promptSpec = promptSpecService.findByName("convert-classinfo-description-to-json").get();
//        var prompt = promptSpec
//                .setVariable("description", description)
//                .withOutputParser(ClassInfo.class)
//                .withTemperature(0.60f)
//                .build();
//
//        Generation generation = chatClient.call(prompt).getResult();
//        return promptSpec.parse(generation.getOutput().getContent(), ClassInfo.class);
//    }
//
//}
