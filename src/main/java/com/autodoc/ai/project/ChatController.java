package com.autodoc.ai.project;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChatService chatService;

    @GetMapping("{projectId}")
    public String getChatPage(@PathVariable("projectId") Long id, Model model, HttpServletRequest request) {
        var project = projectService.findProjectById(id);
        if(project.isPresent()) {
            model.addAttribute("project", projectService.findProjectById(id).get());
        } else {
            model.addAttribute("project", new ProjectEntity());
        }
        return "chat";
    }

    @PostMapping("{id}/message")
    @ResponseBody
    public ResponseEntity<Void> sendProjectMessage(@PathVariable("id") Long projectId, String message) {
        try {
            chatService.process(message, projectId);
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
