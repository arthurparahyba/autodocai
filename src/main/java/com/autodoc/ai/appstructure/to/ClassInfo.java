package com.autodoc.ai.appstructure.to;

import com.autodoc.ai.shared.doc.CodePurpouse;

import java.util.List;

public record ClassInfo(
        String nomeClasse,
        String nomePacote,
        List<Attribute> attributes
) {
    public String fqn() {
        return nomePacote + "." + nomeClasse;
    }
}