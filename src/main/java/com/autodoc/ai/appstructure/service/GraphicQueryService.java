package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.prompt.GenerateDiagramPrompt;
import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GraphicQueryService {

    private static final Logger logger = LoggerFactory.getLogger(GraphicQueryService.class);

    @Autowired
    private ExecuteQueryService executeQueryService;

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;

    @Autowired
    private MessageService messageService;

    public void generate(String userMessage, Long applicationId) {
        var tagMessage = MessageService.createTag();
        try {

            messageService.send(applicationId, tagMessage);

            var query = promptBuilderFactory.builder()
                    .toPrompt("generate-diagram-query-description", Map.of("applicationId", applicationId.toString(), "userMessage", userMessage))
                    .toPrompt("generate-cypher-from-description")
                    .build(CypherResponse.class);
            logger.info("Consulta gerada: \n%s".formatted(query));

            var data = executeQueryService.execute(query.cypherText);
            logger.info("Resultado da consulta: \n%s".formatted(data));

            var response = promptBuilderFactory.builder()
                    .toPrompt("generate-mermaid-diagram", Map.of("data", data))
                    .build(MermaidResponse.class);
            logger.info("Mermaid: \n%s".formatted(response.mermaidText));

            messageService.send(applicationId, tagMessage.toGraphicMessage(response.mermaidText));
        }catch(Exception e) {
            logger.error("Erro no processamento do gráfico", e);
            messageService.send(applicationId, tagMessage.toTextMessage("Erro no processamento do gráfico "));
            messageService.send(applicationId, tagMessage.toTextMessage("[END]"));

        }

    }

    @JsonClassDescription("Objeto representa a resposta a ser enviada para o usuário com o código mermaid")
    public record MermaidResponse(@JsonPropertyDescription("Código mermaid") String mermaidText) {};

    public record CypherResponse(@JsonPropertyDescription("Código Cypher") String cypherText) {};
}
