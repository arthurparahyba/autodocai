package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.project.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueryDocumentPrompt {

    private static final Logger logger = LoggerFactory.getLogger(QueryDocumentPrompt.class);
    @Autowired
    private OpenAiChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private MessageService messageService;

    @Value("classpath:/docs/prompts/prompt-query-documents.txt")
    private Resource promptQueryDocuments;

    public void execute(String message, Long projectId, MessageService.TagMessage tag) {
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
                        .withTemperature(1.0f)
                        //.withFunction("getFileContent")
                        .build()
        );

        final StringBuilder accumulatedContent = new StringBuilder();
        Flux<ChatResponse> responseFlux = chatClient.stream(prompt);
        responseFlux.concatMap(chatResponse -> {
            logger.info(chatResponse.getResult().getOutput().getContent());
            var response = chatResponse.getResult().getOutput().getContent();

            if(response == null) {
                messageService.send(projectId, tag.toTextMessage(accumulatedContent.toString()));
                messageService.send(projectId, tag.toTextMessage("[END]"));
                return Mono.empty();
            }

            accumulatedContent.append(response);

            if (response.contains("\n")) {
                if (!accumulatedContent.toString().isEmpty()) {
                    messageService.send(projectId, tag.toTextMessage(accumulatedContent.toString()));
                    accumulatedContent.setLength(0);
                }
            }
            return Mono.empty();
        }).subscribe();


//        var response = chatClient.call(prompt);
//        return response.getResult().getOutput().getContent();
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
