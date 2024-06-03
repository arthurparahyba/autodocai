package com.autodoc.ai.utils;

import com.autodoc.ai.appstructure.to.*;
import com.autodoc.ai.appsummary.prompt.GenerateFileCodePrompt;
import com.autodoc.ai.promptmanager.model.PromptEntityValidatorOutput;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.parser.BeanOutputParser;

import java.util.List;

class FileUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(FileUtilsTest.class);

    record MethodList(List<Method> metodos){};

    @Test
    public void testListOfFolders() throws JsonProcessingException {
        //final var outputParser = new BeanOutputParser(MethodCallsList.class);
        //final var outputParser = new BeanOutputParser(PromptEntityValidatorOutput.class);
//        final var outputParser = new BeanOutputParser(ClassInfo.class);
//        final var outputParser = new BeanOutputParser(ClassLayerInfo.class);
        final var outputParser = new BeanOutputParser(MethodParameterCallsList.class);

        logger.info(outputParser.getFormat());

    }

}