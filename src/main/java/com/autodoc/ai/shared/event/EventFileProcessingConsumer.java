package com.autodoc.ai.shared.event;

import java.nio.file.Path;
import java.util.List;

public interface EventFileProcessingConsumer {
    void incrementProcessedFiles(Long appId, String serviceName);
    boolean consumeFilesProcessFinished(Long appId, String serviceName);
    List<Path> getPaths(Long appId);
}
