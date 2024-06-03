package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
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
import java.util.Map;
import java.util.Optional;

@Component
public class GenerateFileGenericDocPrompt implements FilePromptProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileGenericDocPrompt.class);

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;

    public Optional<Object> execute(Path path, String fileContent) {
        var response = promptBuilderFactory.builder()
                .toPrompt("generate-generic-documentation-file", "code")
                .build(Response.class)
                .execute(Map.of("code", fileContent));

        return Optional.of(response);
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
    ){};

}
