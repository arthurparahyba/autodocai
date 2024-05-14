package com.autodoc.ai.appstructure.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
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
    private String id;
    private String className;
    private String packageName;
    @JsonIgnore
    @Relationship(type="DEPENDS") private List<CodeEntity> dependencies = new ArrayList<>();
    @JsonIgnore
    @Relationship(type="HAS_FUNCTION") private List<Function> functions = new ArrayList<>();
    @JsonIgnore
    @Relationship(type="HAS_FIELD") private List<Field> fields = new ArrayList<>();

    public CodeEntity() {
    }

    public CodeEntity(Long appId, String className, String packageName, List<CodeEntity> dependencies, List<Function> functions, List<Field> fields) {
        this.id = className+"-"+appId;
        this.className = className;
        this.packageName = packageName;
        this.dependencies = dependencies;
        this.functions = functions;
        this.fields = fields;
    }

    public CodeEntity(Long appId, String className, String packageName) {
        this.id = className+"-"+appId;
        this.className = className;
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<CodeEntity> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<CodeEntity> dependencies) {
        this.dependencies = dependencies;
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

    public void setFields(List<Field> fields) {
        this.fields = fields;
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
