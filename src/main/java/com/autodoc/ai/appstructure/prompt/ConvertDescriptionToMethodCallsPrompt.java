//package com.autodoc.ai.appstructure.prompt;
//
//import com.autodoc.ai.appstructure.to.MethodCallsList;
//import com.autodoc.ai.appstructure.to.MethodParameters;
//import com.autodoc.ai.promptmanager.service.PromptSpecService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.ChatClient;
//import org.springframework.ai.chat.Generation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ConvertDescriptionToMethodCallsPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(ConvertDescriptionToMethodCallsPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    @Autowired
//    private PromptSpecService promptSpecService;
//
//    public MethodCallsList process(String description) {
//        logger.info("\nConvertendo descrição de chamadas de método: \n %s".formatted(description));
//        var promptSpec = promptSpecService.findByName("convert-method-calls-description-to-json").get();
//        var prompt = promptSpec
//                .setVariable("code", description)
//                .withOutputParser(MethodCallsList.class)
//                .withTemperature(0.60f)
//                .build();
//
//        Generation generation = chatClient.call(prompt).getResult();
//        logger.info("\nConversão conluída: \n %s".formatted(generation.getOutput().getContent()));
//        return promptSpec.parse(generation.getOutput().getContent(), MethodCallsList.class);
//    }
//
//}
