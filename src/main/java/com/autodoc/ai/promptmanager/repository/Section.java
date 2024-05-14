package com.autodoc.ai.promptmanager.repository;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(mappedBy = "sections")
    private List<PromptSpec> prompts;

    public Section() {
    }

    public Section(String name, String content, List<PromptSpec> prompts) {
        this.name = name;
        this.content = content;
        this.prompts = prompts;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PromptSpec> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<PromptSpec> prompts) {
        this.prompts = prompts;
    }
}

