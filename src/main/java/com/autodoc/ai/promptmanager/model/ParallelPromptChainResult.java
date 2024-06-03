package com.autodoc.ai.promptmanager.model;

import java.util.Map;

public class ParallelPromptChainResult {

    private Map<Class, Object> result;

    public ParallelPromptChainResult(Map<Class, Object> result) {
        this.result = result;
    }

    public <R> R get(Class<R> type) {
        return (R)this.result.get(type);
    }
}
