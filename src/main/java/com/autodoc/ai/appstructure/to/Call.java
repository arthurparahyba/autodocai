package com.autodoc.ai.appstructure.to;

public record Call(
        String function,
        String className,
        String packageName
) {
    public String fqn() {
        return packageName + "." + className;
    }
}