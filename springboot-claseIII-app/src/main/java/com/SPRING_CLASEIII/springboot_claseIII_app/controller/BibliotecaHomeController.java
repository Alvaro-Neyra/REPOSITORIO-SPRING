package com.SPRING_CLASEIII.springboot_claseIII_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BibliotecaHomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
