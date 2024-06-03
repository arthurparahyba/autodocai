package com.autodoc.ai.appstructure.listener;

import com.autodoc.ai.appstructure.event.FinishedGraphProcessEvent;
import com.autodoc.ai.appstructure.service.AppStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FinishedGraphProcessListener {

    @Autowired
    private AppStructureService appStructureService;

    @Async
    @EventListener
    public void handleCreatedFileDocEvent(FinishedGraphProcessEvent event) {
        appStructureService.process(event.getAppId());
    }
}
