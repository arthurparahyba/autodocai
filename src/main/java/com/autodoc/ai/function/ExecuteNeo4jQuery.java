package com.autodoc.ai.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ExecuteNeo4jQuery  implements Function<ExecuteNeo4jQuery.Request, ExecuteNeo4jQuery.Response> {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteNeo4jQuery.class);

    private Driver driver;

    public ExecuteNeo4jQuery(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Response apply(Request request) {
        var cypherQuery = request.cypherQuery;
        logger.info("Executando a query: "+ cypherQuery);
        try (Session session = driver.session()) {
            Result result = session.run(cypherQuery);
            StringBuilder csv = new StringBuilder();

            csv.append(result.keys().stream().collect(Collectors.joining(";"))).append("\n");

            result.list().forEach(record -> {
                csv.append(record.values().stream()
                                .map(value -> convertValue(value))
                                .collect(Collectors.joining(";")))
                        .append("\n");
            });

            logger.info("Retornando os dados: \n"+csv.toString());
            return new Response(csv.toString());
        } catch (Exception e) {
            logger.error("Erro ao processar a query.", e);
            return new Response("Erro ao executar a consulta ao banco:" + e.getMessage());
        }
    }

    private String convertValue(Value value) {
        if (value.isNull()) {
            return "";
        } else if (value.type().name().equals("LIST")) {
            return value.asList().toString();
        } else if (value.type().name().equals("STRING")) {
            return value.asString();
        } else if (value.type().name().equals("INTEGER")) {
            return String.valueOf(value.asInt());
        } else if (value.type().name().equals("FLOAT")) {
            return String.valueOf(value.asFloat());
        } else if (value.type().name().equals("BOOLEAN")) {
            return String.valueOf(value.asBoolean());
        } else if (value.type().name().equals("NODE")) {
            var node = value.asNode();
            return Map.of(
                    "labels", node.labels(),
                    "properties", node.asMap()
            ).toString();
        } else if (value.type().name().equals("RELATIONSHIP")) {
            var rel = value.asRelationship();
            return Map.of(
                    "type", rel.type(),
                    "startNodeId", rel.startNodeId(),
                    "endNodeId", rel.endNodeId(),
                    "properties", rel.asMap()
            ).toString();
        } else {
            return value.toString();
        }
    }

    @JsonClassDescription("Query neo4j.")
    public record Request(String cypherQuery) {}

    @JsonClassDescription("Resultado da query em csv com header na primeira linha.")
    public record Response(String response) {}
}
