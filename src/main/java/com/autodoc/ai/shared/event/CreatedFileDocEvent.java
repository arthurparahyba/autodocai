package com.autodoc.ai.shared.event;

import com.autodoc.ai.shared.util.ProjectFileUtil;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class CreatedFileDocEvent extends ApplicationEvent {

    private ProjectFileUtil.ProjectFile fileDoc;
    private Long appId;

    public CreatedFileDocEvent(Object source, Long appId, ProjectFileUtil.ProjectFile fileDoc) {
        super(source);
        this.fileDoc = fileDoc;
        this.appId = appId;
    }

    public ProjectFileUtil.ProjectFile getFileDoc() {
        return fileDoc;
    }

    public Long getAppId() {
        return appId;
    }
}
