package com.nuex.controller;

import com.nuex.entity.User;
import com.nuex.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ChatControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    @WithMockUser
    public void testChatEndpoint() throws Exception {
        // Mock service layer response
        when(chatService.chat(any(User.class), anyString()))
            .thenReturn("This is a test response from AI");

        // Send POST request and verify
        mockMvc.perform(post("/api/chat")
                .contentType("application/json")
                .content("{\"message\":\"Hello\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("This is a test response from AI"));
    }

    @Test
    @WithMockUser
    public void testChatEndpointWithEmptyMessage() throws Exception {
        logger.debug("=== Start testing empty message handling ===");
        logger.debug("1. Preparing to send empty message request...");
        
        mockMvc.perform(post("/api/chat")
                .contentType("application/json")
                .content("{\"message\":\"\"}"))
            .andDo(result -> {
                logger.debug("2. Request sent, getting response details:");
                logger.debug("   Request URL: {}", result.getRequest().getRequestURI());
                logger.debug("   Request Method: {}", result.getRequest().getMethod());
                logger.debug("   Request Content: {}", result.getRequest().getContentAsString());
                logger.debug("   Response Status: {}", result.getResponse().getStatus());
                logger.debug("   Response Content: {}", result.getResponse().getContentAsString());
            })
            .andExpect(status().isBadRequest());
        
        logger.debug("3. Test completed, verification passed: Empty message returns 400 status code");
    }
}