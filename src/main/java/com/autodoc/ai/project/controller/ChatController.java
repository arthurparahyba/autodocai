package com.autodoc.ai.project.controller;

import com.autodoc.ai.appstructure.service.GraphicQueryService;
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

    @Autowired
    private GraphicQueryService graphicQueryService;

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

//    @GetMapping("graphic/{projectId}")
//    public String getGraphicChatPage(@PathVariable("projectId") Long id, Model model, HttpServletRequest request) {
//        var project = projectService.findProjectById(id);
//        if(project.isPresent()) {
//            model.addAttribute("project", project.get());
//            model.addAttribute("projectId", id);
//        } else {
//            model.addAttribute("project", new ProjectEntity());
//        }
//        return "chat-document";
//    }

    @PostMapping("{id}/document/message")
    @ResponseBody
    public ResponseEntity<Void> sendProjectMessage(@PathVariable("id") Long projectId, String message) {
        try {
            chatService.process(message, projectId);
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("{id}/graphic/message")
    @ResponseBody
    public ResponseEntity<Void> sendGraphictMessage(@PathVariable("id") Long projectId, String message) {
        try {
            graphicQueryService.generate(message, projectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro ao gerar gráfico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
