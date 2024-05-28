//package com.autodoc.ai.appstructure.prompt;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.ChatClient;
//import org.springframework.ai.chat.Generation;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.chat.prompt.PromptTemplate;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.parser.BeanOutputParser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class GetCodePathPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(GetCodePathPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    public List<Path> process(List<Path> paths) {
//
//        var pathsAsString = paths.stream()
//                .map(Path::toString)
//                .collect(Collectors.joining(","));
//
//        var message = """
//            Please analyze the following list of paths and return only those that belong to the application's source code, excluding paths that point to resources, configuration files, or other auxiliary files.
//
//            == Paths
//            {pathList}
//            == End Paths
//
//            Please consider that source code typically includes files with specific extensions, such as `.java`, `.py`, `.go`, `.js`, and other programming language extensions.
//
//            Also consider that resources or auxiliary files typically include configuration files, templates, or data files, and have extensions such as `.xml`, `.json`, `.html`, and others.
//
//            {format}"
//        """;
//
//        var outputParser = new BeanOutputParser<>(PathList.class);
//
//        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("pathList", pathsAsString, "format", outputParser.getFormat()));
//        Prompt prompt = promptTemplate.create();
//
//        var options = OpenAiChatOptions.builder()
//                .withModel("gpt-3.5-turbo")
//                .withTemperature(0.4f)
//                .build();
//        Prompt promptGpt4 = new Prompt(prompt.getInstructions().get(0), options);
//        Generation generation = chatClient.call(promptGpt4).getResult();
//
//        PathList pathList = outputParser.parse(generation.getOutput().getContent());
//        return pathList.toPaths();
//    }
//
//    public record PathList(List<String> paths){
//        public List<Path> toPaths() {
//            return this.paths.stream()
//                    .map(Path::of)
//                    .collect(Collectors.toList());
//        }
//    }
//}
