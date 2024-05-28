package com.autodoc.ai.shared.event;

import com.autodoc.ai.shared.util.ProjectFileUtil;
import org.springframework.context.ApplicationEvent;

public class LoadedFileContentEvent extends ApplicationEvent {

    private ProjectFileUtil.ProjectFileContent fileContent;
    private Long appId;

    public LoadedFileContentEvent(Object source, Long appId, ProjectFileUtil.ProjectFileContent fileContent) {
        super(source);
        this.fileContent = fileContent;
        this.appId = appId;
    }

    public ProjectFileUtil.ProjectFileContent getFileContent() {
        return fileContent;
    }

    public Long getAppId() {
        return appId;
    }
}
