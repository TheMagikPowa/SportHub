package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class HubController {

    @GetMapping("")
    public String showHub() {
        return "hub/hub";
    }

    @GetMapping("/create-post")
    public String showCreatePost() {
        return "hub/create-post";
    }
}
