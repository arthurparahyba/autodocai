//package com.autodoc.ai.appstructure.prompt;
//
//import com.autodoc.ai.project.util.ProjectFileUtil;
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
//@Component @Deprecated
//public class GenerateContextsPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(GenerateContextsPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    public PathList process(ProjectFileUtil.ProjectFolder folder) {
//        var filesPath = folder.getFilesPath();
//
//        var paths = filesPath.stream()
//                .map(path -> folder.folderPath().relativize(path))
//                .map(Path::toString)
//                .collect(Collectors.joining(","));
//
//        var message = """
//            Please analyze the following list of paths (file and folder paths) from a software project. This list represents the project's structure. Your task is to identify and categorize the contexts or modules present in the application based on this structure.
//
//            Instructions:
//
//            Contexts (Domain Driven Design):
//            - A context represents a logical or functional domain within the application. For example, a "Users" context might contain functionalities related to users, such as creation, authentication, and management.
//            - For each context identified, provide the context's name and corresponding path.
//            - Avoid Duplicate Contexts: Ensure that each context is unique, even if it appears multiple times in the path list.
//            Module Structure:
//            - Consider the name of the folder or directory to define each context's name.
//            - Ignore test folders, static resources, and submodules when identifying contexts.
//            - Ignore Root Folder: Do not consider the root folder when identifying contexts.
//
//            Path List: {pathList}
//
//            {format}"
//        """;
//
//        var outputParser = new BeanOutputParser<>(PathList.class);
//
//        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("pathList", paths.toString(), "format", outputParser.getFormat()));
//        Prompt prompt = promptTemplate.create();
//
//        var options = OpenAiChatOptions.builder()
//                .withModel("gpt-4-turbo")
//                .withTemperature(0.4f)
//                .build();
//        Prompt promptGpt4 = new Prompt(prompt.getInstructions().get(0), options);
//        Generation generation = chatClient.call(promptGpt4).getResult();
//
//        PathList pathList = outputParser.parse(generation.getOutput().getContent());
//        return pathList;
//    }
//
//    public record PathList(List<PathItem> paths){}
//    record PathItem(String contextName, String path){};
//}
