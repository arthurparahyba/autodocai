package com.autodoc.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

@SpringBootApplication
@EnableAsync
public class AiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

	@Bean
	@ConfigurationProperties("spring.datasource.pgvector")
	public DataSourceProperties pgvectorDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource pgvectorDataSource() {
		return pgvectorDataSourceProperties()
				.initializeDataSourceBuilder()
				.build();
	}

	@Bean
	@Primary
	public JdbcTemplate pgvectorJdbcTemplate(@Qualifier("pgvectorDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
