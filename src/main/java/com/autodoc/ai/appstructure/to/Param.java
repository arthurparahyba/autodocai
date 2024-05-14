package com.autodoc.ai.appstructure.to;

public record Param(
        String name,
        String className,
        String packageName
) {
    public String fqn() {
        return packageName + "." + className;
    }
}
