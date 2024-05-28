package com.autodoc.ai.appsummary.prompt;

public sealed interface FilePromptResponse permits GenerateFileCodePrompt.Response, GenerateFileGenericDocPrompt.Response {
    String getResume();
    String getDocumentation();
}
