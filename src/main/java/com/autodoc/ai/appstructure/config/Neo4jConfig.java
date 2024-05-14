package com.autodoc.ai.appstructure.config;

import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "com.autodoc.ai.appstructure")
public class Neo4jConfig {
    @Bean
    public Neo4jTransactionManager transactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }

//    @Bean
//    org.neo4j.cypherdsl.core.renderer.Configuration cypherDslConfiguration() {
//        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
//                .withDialect(Dialect.NEO4J_5).build();
//    }
}
