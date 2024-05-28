package com.autodoc.ai.appstructure.to;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Param(
        String nome,

        @JsonPropertyDescription("Nome da classe do tipo especifico")
        String nomeClasse,
        @JsonPropertyDescription("Nome do pacote do tipo especifico")
        String nomePacote,
        String tipoColecao
) {
    public String fqn() {
        return nomePacote + "." + nomeClasse;
    }
}
