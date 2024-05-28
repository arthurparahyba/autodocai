package com.autodoc.ai.shared.prompt;

import org.springframework.ai.chat.prompt.Prompt;

public class PromptErrorLogger {
    public static String errorMessage(String response, Prompt prompt) {
        return """
                \n==============================================================================
                Erro ao processar o parse do conteúdo retornado.
                Resposta da LLM:
                %s
                
                Conteúdo do prompt:
                %s
                
                ==============================================================================
                """.formatted(response, prompt.getContents());
    }
}
