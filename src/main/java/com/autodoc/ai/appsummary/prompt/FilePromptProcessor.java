package com.autodoc.ai.appsummary.prompt;

import java.nio.file.Path;
import java.util.Optional;

public interface FilePromptProcessor {

    Optional<Object> execute(Path path, String fileContent);

    boolean accept(Path path);
}
