package com.avdei.spring1app.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Getter
@Setter
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String index() {
        return "redirect:/tasks";
    }
}
