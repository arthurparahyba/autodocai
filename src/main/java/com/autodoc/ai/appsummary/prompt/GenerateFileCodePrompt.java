package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.promptmanager.service.PromptSpecService;
import com.autodoc.ai.shared.doc.CodeCategory;
import com.autodoc.ai.shared.doc.CodePurpouse;
import com.autodoc.ai.shared.prompt.PromptErrorLogger;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GenerateFileCodePrompt implements FilePromptProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileCodePrompt.class);

    static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList(
            ".java", ".py", ".cpp", ".c", ".cs", ".js", ".ts", ".rb", ".go", ".php", ".swift", ".kt"
    );

    @Value("${spring.ai.openai.api-key}")
    private String openApiKey;

    @Autowired
    private PromptSpecService promptSpecService;

    public Optional<FilePromptResponse> execute(Path path, String fileContent) {
        var openAiApi = new OpenAiApi(openApiKey);
        var chatClient = new OpenAiChatClient(openAiApi);

        var promptSpec = promptSpecService.findByName("generate-file-code-doc").get();
        var prompt = promptSpec
                .withOutputParser(Response.class)
                .setVariable("fileContent", fileContent)
                .setVariable("filePath", path.toString())
                .withTemperature(0.80f)
                .build();

        var response = chatClient.call(prompt).getResult().getOutput().getContent();

        Prompt resumePrompt = new Prompt("Gere um resumo do conteúdo a seguir: %s".formatted(response));
        var resume = chatClient.call(resumePrompt).getResult().getOutput().getContent();
        return Optional.of(new Response(response, resume));
    }

    @Override
    public boolean accept(Path path) {
        if (path == null) {
            return false;
        }

        return ACCEPTED_EXTENSIONS.stream()
                .anyMatch(extension -> path.toString().toLowerCase().endsWith(extension));
    }


    public record Response(
            @JsonPropertyDescription("Descrição detodos os detalhes da classe.")
            String detalhamentoClasse,
            @JsonPropertyDescription("Explicação resumida da classe.")
            String resumo

    )  implements FilePromptResponse{
        public String getResume() {
            return this.resumo;
        }

        public String getDocumentation() {
            return this.detalhamentoClasse;
        }
    };

}
