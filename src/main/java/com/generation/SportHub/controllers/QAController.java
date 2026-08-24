package com.generation.SportHub.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Person;
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
    public String showQA() {
        return "qna/qna";
        
    }

    @GetMapping("/create-question")
    public String showCreatePost(Model model) {
        model.addAttribute("questionQA", new QuestionQA());
        return "qna/create-qna";
    }

    @PostMapping("/save-post")
    public String createQuestion(@ModelAttribute QuestionQA questionQA,
                                 @AuthenticationPrincipal Buyer currentBuyer,
                                 Model model) {
        try {
           
            qQAService.saveQuestion(questionQA, currentBuyer);
   
            return "redirect:/qa";
            
        } catch (Exception e) {
           
            model.addAttribute("errorMessage", "An error occured during the creation of the post: " + e.getMessage());
            return "error/errorPage";
        }
    }

    @PostMapping("/{questionId}/answer")
    public String addAnswer(@PathVariable Long questionId, @RequestParam String text,@AuthenticationPrincipal Person currentStaffUser) {
        
        qQAService.addAnswerToQuestion(questionId, text, currentStaffUser);
        return "redirect:/qa";
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