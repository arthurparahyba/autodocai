package com.autodoc.ai.appstructure.repository;

import com.autodoc.ai.appstructure.to.ClassLayer;
import com.autodoc.ai.shared.doc.CodePurpouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Node("CodeEntity")
public class CodeEntity{
    @Id
    private final String id;
    private String className;
    private String packageName;

    @JsonIgnore
    @Relationship(type="HAS_FUNCTION")
    private List<Function> functions = new ArrayList<>();

    @JsonIgnore
    @Relationship(type="HAS_FIELD")
    private List<Field> fields = new ArrayList<>();

    @JsonIgnore
    @Relationship(type="HAS_CODE_ENTITY", direction = Relationship.Direction.INCOMING)
    private Layer layer;


    public CodeEntity(Long appId, String className, String packageName, List<Function> functions, List<Field> fields, Layer layer) {
        this.id = appId+"."+packageName+"."+className;
        this.className = className;
        this.packageName = packageName;
        this.functions = functions;
        this.fields = fields;
        this.layer = layer;
    }

    public CodeEntity(Long appId, String className, String packageName, Layer layer) {
        this.id = appId+"."+packageName+"."+className;
        this.className = className;
        this.packageName = packageName;
        this.layer = layer;
    }

    public CodeEntity(Long appId, String className, String packageName) {
        this.id = appId+"."+packageName+"."+className;
        this.className = className;
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        functions.stream().forEach(function -> function.setParentCodeEntity(this));
        this.functions = functions;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeEntity that = (CodeEntity) o;
        return Objects.equals(className, that.className) && Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, packageName);
    }

    public String fqn() {
        return this.packageName + "." + this.className;
    }

    public void addFunction(Function function) {
        this.functions.add(function);
    }

    public Optional<Function> findFunctionByName(String name) {
        return this.getFunctions().stream()
            .filter(f -> f.getName().equalsIgnoreCase(name))
            .findFirst();

    }
}
