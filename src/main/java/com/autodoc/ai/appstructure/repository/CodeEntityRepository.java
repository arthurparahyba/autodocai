package com.autodoc.ai.appstructure.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeEntityRepository extends Neo4jRepository<CodeEntity, String> {

    @Query("MATCH (a:Application)-[:DEPENDS]->(c:CodeEntity) " +
            "WHERE a.id = :#{#idApplication} " +
            "RETURN c.id as id, c.className as className, c.packageName as packageName")
    List<CodeEntityProjection> findCodeEntitiesByApplicationId(@Param("idApplication") Long idApplication);

//    @Query("MATCH (c:CodeEntity {id: $id}) " +
//            "SET c.module = $module " +
//            "RETURN c")
//    CodeEntity updateModule(@Param("id") String id, @Param("module") String module);

    interface CodeEntityProjection {
        String getId();
        String getClassName();
        String getPackageName();
    }
}
