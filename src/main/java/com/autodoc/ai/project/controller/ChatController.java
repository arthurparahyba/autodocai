package com.autodoc.ai.project.controller;

import com.autodoc.ai.appstructure.service.GraphicQueryService;
import com.autodoc.ai.appsummary.service.SummaryChatService;
import com.autodoc.ai.project.service.ProjectService;
import com.autodoc.ai.project.repository.ProjectEntity;
import com.autodoc.ai.project.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChatService chatService;

    @GetMapping("document/{projectId}")
    public String getDocumentChatPage(@PathVariable("projectId") Long id, Model model, HttpServletRequest request) {
        var project = projectService.findProjectById(id);
        if(project.isPresent()) {
            model.addAttribute("project", project.get());
        } else {
            model.addAttribute("project", new ProjectEntity());
        }
        return "chat-document";
    }

    @PostMapping("{id}/generic/message")
    @ResponseBody
    public ResponseEntity<Void> sendGenericMessage(@PathVariable("id") Long projectId, String message) {
        try {
            chatService.process(message, projectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
