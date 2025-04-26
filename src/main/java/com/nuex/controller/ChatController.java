package com.nuex.controller;

import com.nuex.entity.ChatConfig;
import com.nuex.entity.ChatHistory;
import com.nuex.entity.User;
import com.nuex.service.ChatService;
import com.nuex.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository; // 添加这行

    @PostMapping
    public ResponseEntity<?> chat(
            @AuthenticationPrincipal User user,
            @RequestBody ChatRequest request) {
        try {
            String response = chatService.chat(user, request.getMessage());
            return ResponseEntity.ok().body(response); // 明确设置响应体
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Page<ChatHistory>> getChatHistory(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getChatHistory(user, pageable));
    }

    @PutMapping("/config")
    public ResponseEntity<?> updateConfig(
            @AuthenticationPrincipal User user,
            @RequestBody ChatConfig config) {
        try {
            ChatConfig updatedConfig = chatService.updateChatConfig(user, config);
            return ResponseEntity.ok(updatedConfig);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Data
    public static class ChatRequest {
        private String message;
    }

    @PostMapping("/test")
    public ResponseEntity<String> testChat(@RequestBody String message) {
        try {
            // 先查询是否已存在匿名用户
            User anonymous = userRepository.findByUsername("anonymous")
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername("anonymous");
                    newUser.setPassword("tempPassword");
                    return userRepository.save(newUser);
                });
                
            String response = chatService.chat(anonymous, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
