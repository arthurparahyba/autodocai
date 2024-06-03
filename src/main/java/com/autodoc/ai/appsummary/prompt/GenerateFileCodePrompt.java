package com.autodoc.ai.appsummary.prompt;

import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GenerateFileCodePrompt implements FilePromptProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFileCodePrompt.class);

    static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList(
            ".java", ".py", ".cpp", ".c", ".cs", ".js", ".ts", ".rb", ".go", ".php", ".swift", ".kt"
    );

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;

    public Optional<Object> execute(Path path, String fileContent) {
        var classDescription = promptBuilderFactory.builder()
                .toPrompt("generate-class-documentation", "code")
                .build(ClassDescription.class)
                .execute(Map.of("code", fileContent));

        var methodsDescription = promptBuilderFactory.builder()
                .toPrompt("generate-methods-documentation", "code")
                .build(MethodsDescription.class)
                .execute(Map.of("code", fileContent));

        return Optional.of(new FileCodeResponse(classDescription, methodsDescription));
    }

    @Override
    public boolean accept(Path path) {
        if (path == null) {
            return false;
        }

        return ACCEPTED_EXTENSIONS.stream()
                .anyMatch(extension -> path.toString().toLowerCase().endsWith(extension));
    }

    public record ClassDescription(String className, String packageName, String description, String attributesDescription) {
        public String fqn() {return this.packageName + "." + this.className;}
    };

    public record MethodsDescription(List<MethodDescription> methods){};

    public record MethodDescription(String resume, String sequenceDescription) {};


    public record FileCodeResponse(
            ClassDescription classDescription,
            MethodsDescription methodsDescription

    )  {};

}
