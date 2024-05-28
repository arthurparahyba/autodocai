package com.autodoc.ai.appstructure.to;

public record Dependency(
        String nomeClasse,
        String nomePacote,
        String tipoColecao
) {
    public String fqn() {
        return nomePacote + "." + nomeClasse;
    }
}
