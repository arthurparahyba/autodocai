package com.autodoc.ai.appstructure.repository;

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
    private String id;
    private String className;
    private String packageName;

    @Relationship(type="HAS_FUNCTION", direction = Relationship.Direction.OUTGOING)
    private List<Function> functions = new ArrayList<>();

    @Relationship(type="HAS_FIELD", direction = Relationship.Direction.OUTGOING)
    private List<Field> fields = new ArrayList<>();

    @Relationship(type="BELONGS_TO_LAYER", direction = Relationship.Direction.INCOMING)
    private Layer layer;

    @JsonIgnore
    @Relationship(type="BELONGS_TO_MODULE", direction = Relationship.Direction.INCOMING)
    private Module module;

    public CodeEntity() {
    }

    public CodeEntity(Long appId, String className, String packageName, List<Function> functions, List<Field> fields, Layer layer) {
        this(appId, className, packageName);
        this.functions = functions;
        this.fields = fields;
        this.layer = layer;
    }

    public CodeEntity(Long appId, String className, String packageName, Layer layer) {
        this(appId, className, packageName);
        this.layer = layer;
    }

    public CodeEntity(Long appId, String className, String packageName) {
        this.id = STR."\{appId}.\{packageName}.\{className}";
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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
