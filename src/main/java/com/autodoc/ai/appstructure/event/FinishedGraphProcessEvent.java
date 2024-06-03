package com.autodoc.ai.appstructure.event;

import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;
import java.util.List;

public class FinishedGraphProcessEvent extends ApplicationEvent {
    private Long appId;
    public FinishedGraphProcessEvent(Object source, Long appId) {
        super(source);
        this.appId = appId;
    }

    public Long getAppId() {
        return this.appId;
    }

}
