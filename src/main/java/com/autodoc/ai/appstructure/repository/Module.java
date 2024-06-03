package com.autodoc.ai.appstructure.repository;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Module")
public class Module {
        private @Id String id;

        private String name;

        @Relationship(type = "BELONGS_TO_MODULE", direction = Relationship.Direction.OUTGOING)
        private List<CodeEntity> codeEntities = new ArrayList<>();


        public Module(Long appId, String name) {
                this.id = STR."\{appId}.\{name}";
                this.name = name;
        }

        public String getId() {
                return id;
        }

        public List<CodeEntity> getCodeEntities() {
                return codeEntities;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

}
