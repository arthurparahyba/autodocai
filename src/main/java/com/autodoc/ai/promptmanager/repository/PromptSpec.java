package com.autodoc.ai.promptmanager.repository;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PromptSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    private List<AutodocTool> tools = new ArrayList<>();


    public PromptSpec() {
    }

    public PromptSpec(String name, String content, List<Section> sections, List<AutodocTool> tools) {
        this.name = name;
        this.content = content;
        this.sections = sections;
        this.tools = tools;
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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<AutodocTool> getTools() {
        return tools;
    }

    public void setTools(List<AutodocTool> tools) {
        this.tools = tools;
    }

}

