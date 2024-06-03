package com.autodoc.ai.promptmanager.model;

import com.autodoc.ai.promptmanager.repository.PromptSpec;

public sealed interface IPromptEntity permits PromptEntity, PromptEntityValidator {
    AutodocPromptSpec getPromptSpec();
}
