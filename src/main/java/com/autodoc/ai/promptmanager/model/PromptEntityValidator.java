package com.autodoc.ai.promptmanager.model;

import java.util.Map;

public final class PromptEntityValidator implements IPromptEntity{

    private AutodocPromptSpec promptSpec;

    private Object defaultResponse;

    public PromptEntityValidator(AutodocPromptSpec promptSpec, Object defaultResponse) {
        this.promptSpec = promptSpec;
        this.defaultResponse = defaultResponse;
    }

    @Override
    public AutodocPromptSpec getPromptSpec() {
        return promptSpec;
    }

    public Object getDefaultResponse() {
        return defaultResponse;
    }
}

