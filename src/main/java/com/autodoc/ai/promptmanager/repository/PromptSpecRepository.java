package com.autodoc.ai.promptmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromptSpecRepository extends JpaRepository<PromptSpec, Long> {
    Optional<PromptSpec> findByName(String name);
}



