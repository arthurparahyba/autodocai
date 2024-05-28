package com.autodoc.ai.appstructure.to;

public record Attribute(
        String nomeAtributo,
        String nomeClasse,
        String nomePacote,
        String tipoColecao
) {
    public String fqn() {
        return nomePacote + "." + nomeClasse;
    }
}
