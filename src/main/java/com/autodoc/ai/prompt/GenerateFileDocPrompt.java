package com.autodoc.ai.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Component
public class GenerateFileDocPrompt {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileDocPrompt.class);

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/docs/prompts/prompt-generate-file-doc.txt")
    private Resource promptFileDocExecution;

    public String execute(Path path, String fileContent) {
        var promptTemplate = new SystemPromptTemplate(promptFileDocExecution);
        var systemMessage = promptTemplate.createMessage(Map.of("fileContent", fileContent, "path", path.toString()));

        var prompt = new Prompt(
                List.of(systemMessage),
                OpenAiChatOptions.builder()
                        .withModel("gpt-3.5-turbo")
                        .build());

        var aiExecutionResponse = chatClient.call(prompt);
        return aiExecutionResponse.getResult().getOutput().getContent();
    }

}
