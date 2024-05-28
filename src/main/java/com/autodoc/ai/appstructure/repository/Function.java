package com.autodoc.ai.appstructure.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Function")
public class Function{
        @Id String id;
        String name;
        @Relationship(type="USES") List<CodeEntity> params = new ArrayList<>();

        @Relationship(type="CALL") List<Function> functionsCalled = new ArrayList<>();

        @Relationship(type="HAS_FUNCTION", direction=Relationship.Direction.INCOMING)
        CodeEntity parentCodeEntity;

        @Relationship(type="RETURNS") CodeEntity returnType;


        public Function(Long appId, String name, CodeEntity parentCodeEntity) {
                this.id = appId+"."+parentCodeEntity.getPackageName()+"."+parentCodeEntity.getClassName()+"."+name;
                this.name = name;
                this.parentCodeEntity = parentCodeEntity;
        }

        public Function(Long appId, String name, CodeEntity parentCodeEntity, List<CodeEntity> params) {
                this.id = appId+"."+parentCodeEntity.getPackageName()+"."+parentCodeEntity.getClassName()+"."+name;
                this.name = name;
                this.params = params;
                this.parentCodeEntity = parentCodeEntity;
        }

        public Function(Long appId, String name, CodeEntity parentCodeEntity, List<CodeEntity> params, CodeEntity returnType) {
                this.id = appId+"."+parentCodeEntity.getPackageName()+"."+parentCodeEntity.getClassName()+"."+name;
                this.name = name;
                this.params = params;
                this.parentCodeEntity = parentCodeEntity;
                this.returnType = returnType;
        }

        public Function(Long appId, String name, List<CodeEntity> params, List<Function> functionsCalled, CodeEntity parentCodeEntity, CodeEntity returnType) {
                this.id = appId+"."+parentCodeEntity.getPackageName()+"."+parentCodeEntity.getClassName()+"."+name;
                this.name = name;
                this.params = params;
                this.returnType = returnType;
                this.parentCodeEntity = parentCodeEntity;
        }

        public List<Function> getFunctionsCalled() {
                return functionsCalled;
        }

        public void setFunctionsCalled(List<Function> functionsCalled) {
                this.functionsCalled = functionsCalled;
        }

        public CodeEntity getParentCodeEntity() {
                return parentCodeEntity;
        }

        public void setParentCodeEntity(CodeEntity parentCodeEntity) {
                this.parentCodeEntity = parentCodeEntity;
        }

        public String getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public List<CodeEntity> getParams() {
                return params;
        }

        public void setParams(List<CodeEntity> params) {
                this.params = params;
        }

        public CodeEntity getReturnType() {
                return returnType;
        }

        public void setReturnType(CodeEntity returnType) {
                this.returnType = returnType;
        }

        public void addParam(CodeEntity codeEntity) {
                this.params.add(codeEntity);
        }
}
