package com.autodoc.ai.promptmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PromptEntity  implements IPromptEntity{
    private final String name;
    private final Map<String, String> variables;

    public PromptEntity(String name, Map<String, String> variables) {
        this.name = name;
        this.variables = variables;
    }

    @Override
    public String name() {
        return this.name;
    }

    public Map<String, String> variables() {
        return variables;
    }

}

