package com.autodoc.ai.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueryDocumentPrompt {

    private static final Logger logger = LoggerFactory.getLogger(QueryDocumentPrompt.class);
    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/docs/prompts/prompt-query-documents.txt")
    private Resource promptQueryDocuments;

    public String execute(String message, Long projectId) {
        var searchRequest = SearchRequest.query(message)
                .withFilterExpression("projectId == %s".formatted(projectId))
                .withTopK(10);
        var similarDocuments = this.vectorStore.similaritySearch(searchRequest);

        var systemMessage = createSystemMessage(similarDocuments, projectId);
        var userMessage = new UserMessage(message);


        var prompt = new Prompt(
                List.of(systemMessage, userMessage),
                OpenAiChatOptions.builder()
                        .withModel("gpt-3.5-turbo")
                        .withFunction("getFileContent")
                        .build()
        );


        var response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    private Message createSystemMessage(List<Document> similarDocuments, Long projectId) {
        var documents = getString(similarDocuments);
        logger.info(documents);
        var promptTemplate = new SystemPromptTemplate(promptQueryDocuments);
        return promptTemplate.createMessage(Map.of("documents", documents, "projeto", projectId));
    }

    private static String getString(List<Document> similarDocuments) {
        var documents = similarDocuments.stream()
                .map(entry -> entry.getContent())
                .collect(Collectors.joining("\n"));
        return documents;
    }
}
