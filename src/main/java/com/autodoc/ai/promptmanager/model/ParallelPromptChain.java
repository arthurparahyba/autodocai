package com.autodoc.ai.promptmanager.model;

import java.util.Map;

public interface ParallelPromptChain {
    ParallelPromptChainResult execute(Map<String, String> variables);
}
