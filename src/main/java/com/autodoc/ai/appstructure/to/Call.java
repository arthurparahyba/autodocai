package com.autodoc.ai.appstructure.to;

public record Call(
        String nomeMetodoChamado,
        String nomeClasse,
        String nomePacote
) {
    public String fqn() {
        return nomePacote + "." + nomeClasse;
    }
}