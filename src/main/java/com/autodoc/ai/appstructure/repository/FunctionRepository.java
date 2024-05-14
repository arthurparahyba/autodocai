package com.autodoc.ai.appstructure.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends Neo4jRepository<Function, String> {

}
