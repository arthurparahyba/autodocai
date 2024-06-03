package com.autodoc.ai.appstructure.listener;

import com.autodoc.ai.appstructure.event.FinishedGraphProcessEvent;
import com.autodoc.ai.appstructure.service.AppStructureService;
import com.autodoc.ai.shared.event.EventFileProcessingConsumer;
import com.autodoc.ai.shared.event.EventFileProcessingService;
import com.autodoc.ai.shared.event.LoadedFileContentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class AppStructureFileListener {

    static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList(
            ".java", ".py", ".cpp", ".c", ".cs", ".js", ".ts", ".rb", ".go", ".php", ".swift", ".kt"
    );

    @Autowired
    private AppStructureService appStructureService;

    @Autowired
    private EventFileProcessingConsumer eventFileProcessingConsumer;

    private final Lock lock = new ReentrantLock();

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Async
    @EventListener
    public void handleCreatedFileDocEvent(LoadedFileContentEvent event) {
        try {

            if(!accept(event.getFileContent().path())) {
                return;
            }
            final var codeDoc = event.getFileContent();
            var applicationByDocument = appStructureService.process(event.getAppId(), codeDoc);

            try {
                lock.lock();
                appStructureService.createApplication(applicationByDocument);
            } finally{
                lock.unlock();
            }

        } finally {
            eventFileProcessingConsumer.incrementProcessedFiles(event.getAppId(), getClass().getName());
            if(eventFileProcessingConsumer.consumeFilesProcessFinished(event.getAppId(), getClass().getName())) {
                eventPublisher.publishEvent(new FinishedGraphProcessEvent(this, event.getAppId()));
            }
        }

    }

    private boolean accept(Path path) {
        if (path == null) {
            return false;
        }
        return ACCEPTED_EXTENSIONS.stream()
                .anyMatch(extension -> path.toString().toLowerCase().endsWith(extension));
    }
}
