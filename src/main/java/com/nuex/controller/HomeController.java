package com.nuex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home"; // 对应templates/home.html
    }
}