package com.autodoc.ai.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectDocumentationService documentationService;


    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectDocumentationService documentationService) {
        this.projectRepository = projectRepository;
        this.documentationService = documentationService;
    }

    public ProjectEntity saveProject(ProjectEntity projeto) {
        projeto.setPath(Path.of(projeto.getPath()).toString());
        var newProject = projectRepository.save(projeto);
        this.documentationService.generateDocs(newProject);
        return newProject;
    }

    public List<ProjectEntity> findAll() {
        return projectRepository.findAllByIdDesc();
    }

    public Optional<ProjectEntity> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Optional<ProjectEntity> findProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
