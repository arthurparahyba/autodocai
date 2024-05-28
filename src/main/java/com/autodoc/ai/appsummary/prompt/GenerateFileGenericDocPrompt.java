package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.promptmanager.service.PromptSpecService;
import com.autodoc.ai.shared.prompt.PromptErrorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Optional;

@Component
public class GenerateFileGenericDocPrompt implements FilePromptProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileGenericDocPrompt.class);

    @Autowired
    private ObjectProvider<ChatClient> chatClientProvider;

    @Autowired
    private PromptSpecService promptSpecService;

    public Optional<FilePromptResponse> execute(Path path, String fileContent) {
        var chatClient = chatClientProvider.getObject();

        var promptSpec = promptSpecService.findByName("generate-file-doc").get();
        var prompt = promptSpec
                .withOutputParser(Response.class)
                .setVariable("fileContent", fileContent)
                .setVariable("filePath", path.toString())
                .withTemperature(0.60f)
                .build();

        Generation generation = chatClient.call(prompt).getResult();
        try {
            return Optional.of(promptSpec.parse(generation.getOutput().getContent(), Response.class));
        }catch(RuntimeException e) {
            var message = PromptErrorLogger.errorMessage(generation.getOutput().getContent(), prompt);
            logger.error(message, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean accept(Path path) {
        if (path == null) {
            return false;
        }

        return !GenerateFileCodePrompt.ACCEPTED_EXTENSIONS.stream()
                .anyMatch(extension -> path.toString().toLowerCase().endsWith(extension));
    }

    public record Response(
            String resume,
            String description
    ) implements FilePromptResponse{
        public String getResume() {
            return this.resume;
        }

        public String getDocumentation() {
            return this.description;
        }
    };

}
