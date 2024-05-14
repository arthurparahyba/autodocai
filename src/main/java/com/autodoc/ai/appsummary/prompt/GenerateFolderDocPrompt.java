package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.shared.util.ProjectFileUtil;
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
public class GenerateFolderDocPrompt {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFolderDocPrompt.class);

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/docs/prompts/prompt-generate-folder-doc.txt")
    private Resource promptFolderDocExecution;

    public String execute(Path path, List<ProjectFileUtil.ProjectFile> files, List<ProjectFileUtil.ProjectFolder> folders) {
        var promptTemplate = new SystemPromptTemplate(promptFolderDocExecution);

        StringBuffer buffer = new StringBuffer();
        files.stream()
                .forEach(file -> {
                    buffer.append("== Documentação do arquivo: "+file.path()+"\n");
                    buffer.append(file.documentation().resume() + "\n");
                    buffer.append("== Fim \n");
                });

        folders.stream()
                .forEach(folder -> {
                    buffer.append("== Documentação da pasta: "+folder.folderPath()+"\n");
                    buffer.append(folder.documentation() + "\n");
                    buffer.append("== Fim \n");
                });

        var systemMessage = promptTemplate.createMessage(Map.of("fileDocuments", buffer.toString(), "path", path.toString()));

        var prompt = new Prompt(
                List.of(systemMessage),
                OpenAiChatOptions.builder()
                        .withModel("gpt-3.5-turbo")
                        .build());

        var aiExecutionResponse = chatClient.call(prompt);
        return aiExecutionResponse.getResult().getOutput().getContent();
    }

}
