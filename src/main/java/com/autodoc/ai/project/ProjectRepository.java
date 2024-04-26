package com.autodoc.ai.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByName(String nome);

    @Query("SELECT p FROM ProjectEntity p ORDER BY p.id DESC")
    List<ProjectEntity> findAllByIdDesc();
}
