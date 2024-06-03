package com.autodoc.ai.appstructure.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppStructureRepository extends Neo4jRepository<Application, Long> {

    @Query("MERGE (a:Application {id: $id}) " +
            "ON CREATE SET a.name = $name " +
            "RETURN a")
    Application merge(Long id, String name);

    @Query("MERGE (a:Application {id: $id}) " +
            "RETURN a")
    Application merge(Long id);

}
