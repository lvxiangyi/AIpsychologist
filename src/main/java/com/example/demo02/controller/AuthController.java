package com.example.demo02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }
    
    @PostMapping("/register")
    public String processRegistration() {
        // 这里将添加注册逻辑
        return "redirect:/auth/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}