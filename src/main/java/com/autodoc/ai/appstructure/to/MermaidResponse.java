package com.autodoc.ai.appstructure.to;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Objeto representa a resposta a ser enviada para o usuário com o código mermaid")
public record MermaidResponse(@JsonPropertyDescription("Código mermaid") String mermaidText) {};
