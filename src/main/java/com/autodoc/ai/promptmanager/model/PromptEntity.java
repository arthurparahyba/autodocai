package com.autodoc.ai.promptmanager.model;

import com.autodoc.ai.promptmanager.repository.PromptSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PromptEntity  implements IPromptEntity{
    private final AutodocPromptSpec promptSpec;
    private List<String> variableNames;

    public PromptEntity(AutodocPromptSpec promptSpec, String... variableNames) {
        this.promptSpec = promptSpec;
        this.variableNames = List.of(variableNames);
    }

    public PromptEntity(AutodocPromptSpec promptSpec) {
        this.promptSpec = promptSpec;
    }

    @Override
    public AutodocPromptSpec getPromptSpec() {
        return this.promptSpec;
    }

    public List<String> getVariableNames() {return this.variableNames;}

}

