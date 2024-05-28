package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.function.ExecuteNeo4jQuery;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExecuteQueryService {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteQueryService.class);

    @Autowired
    private Driver driver;

    public String execute(String cypherQuery) {
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

            return csv.toString();
        } catch (Exception e) {
            logger.error("Erro ao processar a query.", e);
            return null;
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
}
