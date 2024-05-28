package com.autodoc.ai.appstructure.repository;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Node("Application")
public class Application{
    @Id Long id;
    String name;
    @Relationship(type="DEPENDS") List<CodeEntity> entities;

    @Relationship(type="HAS_LAYER") List<Layer> layers = new ArrayList<>();

    public Application() {
    }

    public Application(Long id, String name, List<CodeEntity> entities) {
        this.id = id;
        this.name = name;
        this.entities = entities;
    }

    public Application(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<CodeEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<CodeEntity> entities) {
        this.entities = entities;
    }

    public Optional<CodeEntity> findCodeEntityByFQN(String fqn) {
        return entities.stream().filter(en -> {
            return en.fqn().equals(fqn);
        }).findFirst();
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }
}
