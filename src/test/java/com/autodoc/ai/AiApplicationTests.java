package com.autodoc.ai;

import com.autodoc.ai.appstructure.prompt.GenerateDiagramPrompt;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@SpringBootTest
class AiApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(AiApplicationTests.class);

	@Test
	void contextLoads() {
		var lista = List.of();
		lista.add("");
	}

}
