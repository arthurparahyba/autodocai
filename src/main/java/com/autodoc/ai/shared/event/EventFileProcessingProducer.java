package com.autodoc.ai.shared.event;

import java.nio.file.Path;
import java.util.List;

public interface EventFileProcessingProducer {
    void setFilesPathEvent(Long appId, List<Path> total);
}
