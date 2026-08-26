package com.generation.SportHub.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.generation.SportHub.entity.QuestionQA;
import com.generation.SportHub.service.QuestionQAService;



@Controller
@RequestMapping("qA")

public class QAController {

    private final QuestionQAService qQAService;

     public QAController(QuestionQAService qQAService) {
        this.qQAService = qQAService;
    }

    @GetMapping("")
    public String showQA(Model model) {
        model.addAttribute("questions", qQAService.findAllQuestions());
        return "qna/qna";
        
    }

    @GetMapping("/create-question")
    public String showCreatePost(Model model) {
        model.addAttribute("questionQA", new QuestionQA());
        return "qna/create-qna";
    }

    @PostMapping("/save-post")
    public String createQuestion(
            @ModelAttribute QuestionQA questionQA,
            Authentication authentication,
            Model model) {

        try {
            qQAService.saveQuestion(questionQA, authentication.getName());
            return "redirect:/qA";

        } catch (Exception e) {
            model.addAttribute(
                "errorMessage",
                "An error occurred during creation: " + e.getMessage()
            );
            return "error/errorPage";
        }
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/{questionId}/answer")
    public String addAnswer(
            @PathVariable Long questionId,
            @RequestParam String text,
            Authentication authentication) {

        qQAService.addAnswerToQuestion(
                questionId,
                text,
                authentication.getName());

        return "redirect:/qA";
    }

    @PostMapping("/delete-question")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String deleteQuestion (@RequestParam("idQuestion") Long id,
            Model model) {
                if (id != null) {
            qQAService.delete(id);
            model.addAttribute("msg", "Question eliminated");
        } else {
            model.addAttribute("msg", "The question doesn't exists");
        }
        return "redirect:/qA";
    }
}
