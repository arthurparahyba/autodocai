package com.autodoc.ai.promptmanager.repository;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AutodocTool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "tools")
    private List<PromptSpec> prompts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PromptSpec> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<PromptSpec> prompts) {
        this.prompts = prompts;
    }
}
