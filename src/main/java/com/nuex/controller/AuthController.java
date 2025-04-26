package com.nuex.controller;

import com.nuex.entity.User;
import com.nuex.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;  // 添加这行
import lombok.extern.slf4j.Slf4j;  // 添加这行

@Slf4j  // 添加类注解
@Controller  // 现在这个注解会被正确识别
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // 新增GET方法用于返回注册页面
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // 返回注册页面的视图名称
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getUsername(), request.getPassword());
            log.info("用户注册成功: {}", user.getUsername()); // 添加日志
            return ResponseEntity.ok().body("注册成功");
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage()); // 添加错误日志
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
    }
}