package com.autodoc.ai.appstructure.listener;

import com.autodoc.ai.appstructure.service.AppStructureService;
import com.autodoc.ai.shared.doc.CodeCategory;
import com.autodoc.ai.shared.event.CreatedFileDocEvent;
import com.autodoc.ai.shared.event.LoadedFileContentEvent;
import com.autodoc.ai.shared.util.ProjectFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class FileDocListener {

    static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList(
            ".java", ".py", ".cpp", ".c", ".cs", ".js", ".ts", ".rb", ".go", ".php", ".swift", ".kt"
    );

    @Autowired
    private AppStructureService appStructureService;

    private final Lock lock = new ReentrantLock();

    @Async
    @EventListener
    public void handleCreatedFileDocEvent(LoadedFileContentEvent event) {
        lock.lock();

        try {
            if(accept(event.getFileContent().path())){
                final var codeDoc = event.getFileContent();
                appStructureService.process(event.getAppId(), codeDoc);
            }
        } finally {
            lock.unlock();
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
