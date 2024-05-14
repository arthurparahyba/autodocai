package com.autodoc.ai.appstructure.to;

import java.util.List;

public record ClassInfo(
        String name,
        String packageName,
        List<Attribute> attributes,
        List<Dependency> dependencies,
        List<Method> methods
) {
    public String fqn() {
        return packageName + "." + name;
    }
}