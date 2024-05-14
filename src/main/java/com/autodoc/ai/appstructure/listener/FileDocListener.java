package com.autodoc.ai.appstructure.listener;

import com.autodoc.ai.appstructure.service.AppStructureService;
import com.autodoc.ai.shared.doc.DocumentationCategory;
import com.autodoc.ai.shared.event.CreatedFileDocEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FileDocListener {

    @Autowired
    private AppStructureService appStructureService;

    @EventListener
    public void handleCreatedFileDocEvent(CreatedFileDocEvent event) {
        if(!DocumentationCategory.BACKEND.equals(event.getFileDoc().documentation().category())){
            return;
        }

        final var fileDoc = event.getFileDoc();
        appStructureService.process(event.getAppId(), fileDoc);
    }
}
