package com.autodoc.ai.appstructure.repository;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;

@Node("Field")
public class Field{
        @Id @GeneratedValue
        Long id;
        String name;
        @JsonBackReference
        @Relationship(type="OF_TYPE") CodeEntity type;

        public Field(String name) {
                this.name = name;
        }

        public Field(String name, CodeEntity type) {
                this.name = name;
                this.type = type;
        }

        public Long getId() {
                return this.id;
        }



        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public CodeEntity getType() {
                return type;
        }

        public void setType(CodeEntity type) {
                this.type = type;
        }
}
