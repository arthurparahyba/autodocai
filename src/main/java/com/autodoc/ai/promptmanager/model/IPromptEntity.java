package com.autodoc.ai.promptmanager.model;

public sealed interface IPromptEntity permits PromptEntity, PromptEntityValidator {
    String name();
}
