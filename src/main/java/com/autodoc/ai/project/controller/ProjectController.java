package com.autodoc.ai.project.controller;

import com.autodoc.ai.project.service.ProjectService;
import com.autodoc.ai.project.repository.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping
    public String showProjects(Model model) {
        List<ProjectEntity> projects = service.findAll();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("form")
    public String showForm(Model model) {
        ProjectEntity p = new ProjectEntity();
        model.addAttribute("project", p);
        return "projects-form";
    }

    @PostMapping
    public String addProject(ProjectEntity project, RedirectAttributes redirectAttributes) {
        var savedProject = service.saveProject(project);
        return "redirect:/chat/document/"+savedProject.getId();
    }
}
