package com.autodoc.ai.appsummary.listener;

import com.autodoc.ai.appsummary.service.SummaryGeneratorService;
import com.autodoc.ai.shared.event.LoadedFileContentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FileContentListener {

    @Autowired
    private SummaryGeneratorService summaryService;

    @Async
    @EventListener
    public void handleLoadedFileContentEvent(LoadedFileContentEvent event) {
        summaryService.process(event.getFileContent());
    }
}
