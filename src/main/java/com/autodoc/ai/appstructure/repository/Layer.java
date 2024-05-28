package com.autodoc.ai.appstructure.repository;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Layer")
public class Layer {
        @Id String name;

        @Relationship(type = "HAS_CODE_ENTITY")
        private List<CodeEntity> codeEntities = new ArrayList<>();


        public Layer(String name) {
                this.name = name;
        }

        public void addCodeEntity(CodeEntity code) {
                this.codeEntities.add(code);
        }

        public void setCodeEntities(List<CodeEntity> codeEntities) {
                this.codeEntities = codeEntities;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

}
