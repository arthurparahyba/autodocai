//package com.autodoc.ai.appstructure.prompt;
//
//import com.autodoc.ai.appstructure.to.ClassInfo;
//import com.autodoc.ai.promptmanager.service.PromptSpecService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.ChatClient;
//import org.springframework.ai.chat.Generation;
//import org.springframework.ai.openai.OpenAiChatClient;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GenerateMethodCallsDescriptionPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(GenerateMethodCallsDescriptionPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    @Autowired
//    private PromptSpecService promptSpecService;
//
//    public String process(String fileContent) {
//        var promptSpec = promptSpecService.findByName("generate-method-calls-description").get();
//        var prompt = promptSpec
//                .setVariable("code", fileContent)
//                .withTemperature(0.60f)
//                .build();
//
//        Generation generation = chatClient.call(prompt).getResult();
//        return generation.getOutput().getContent();
//
//    }
//
//}
