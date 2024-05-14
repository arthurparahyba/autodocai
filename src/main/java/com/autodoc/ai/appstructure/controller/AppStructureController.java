package com.autodoc.ai.appstructure.controller;

import com.autodoc.ai.appstructure.repository.Application;
import com.autodoc.ai.appstructure.repository.AppStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appstructure")
public class AppStructureController {

    @Autowired
    private AppStructureRepository appStructureRepository;

   @GetMapping("/applications")
    public List<Application> getApplications() {
        return appStructureRepository.findAll();
    }

}
