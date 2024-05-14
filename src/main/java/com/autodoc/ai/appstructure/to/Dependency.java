package com.autodoc.ai.appstructure.to;

public record Dependency(
        String className,
        String packageName,
        String collectionType
) {
    public String fqn() {
        return packageName + "." + className;
    }
}
