package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.promptmanager.service.PromptSpecService;
import com.autodoc.ai.shared.doc.DocumentationCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class GenerateFileDocPrompt {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileDocPrompt.class);

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private PromptSpecService promptSpecService;

    public Response execute(Path path, String fileContent) {
        var promptSpec = promptSpecService.findByName("generate-file-doc").get();
        var prompt = promptSpec
                .withOutputParser(Response.class)
                .setVariable("fileContent", fileContent)
                .setVariable("filePath", path.toString())
                .withTemperature(0.60f)
                .build();

        Generation generation = chatClient.call(prompt).getResult();
        return promptSpec.parse(generation.getOutput().getContent(), Response.class);
    }



    public record Response(String classDescription, String attributesDescription, String methodsDescription, String dependencies, DocumentationCategory documentationCategory) {
        public String getResume() {
            return this.classDescription;
        }

        public String getDocumentation() {
            return this.classDescription +
                    "\n\n" +
                    this.attributesDescription +
                    "\n\n" +
                    this.methodsDescription +
                    "\n\n" +
                    this.dependencies +
                    "\n\n" +
                    "Category: "+this.documentationCategory;
        }
    };

}
