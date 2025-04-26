package com.nuex.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuex.entity.ChatConfig;
import com.nuex.entity.ChatHistory;
import com.nuex.entity.User;
import com.nuex.repository.ChatConfigRepository;
import com.nuex.repository.ChatHistoryRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatLanguageModel chatLanguageModel;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatConfigRepository chatConfigRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    @Transactional
    public String chat(User user, String message) {
        logger.debug("Processing chat request from user: {}", user.getUsername());

        // Empty message check
        if (message == null || message.trim().isEmpty()) {
            logger.warn("Empty message received from user: {}", user.getUsername());
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        logger.debug("Message content validation passed");
        
        // Content moderation
        if (containsSensitiveContent(message)) {
            logger.warn("Sensitive content detected in message from user: {}", user.getUsername());
            throw new RuntimeException("Message contains sensitive content");
        }
        logger.debug("Content moderation check passed");

        // Get user config
        ChatConfig config = chatConfigRepository.findByUser(user)
                .orElseGet(() -> {
                    logger.debug("Creating default config for user: {}", user.getUsername());
                    ChatConfig defaultConfig = new ChatConfig();
                    defaultConfig.setUser(user);
                    return chatConfigRepository.save(defaultConfig);
                });
        logger.debug("Using model configuration: {}", config.getModelName());

        // Call AI model
        logger.debug("Sending message to AI model");
        String response = chatLanguageModel.chat(message);
        logger.debug("Received AI response");

        // Save chat history
        ChatHistory history = new ChatHistory();
        history.setUser(user);
        history.setUserMessage(message);
        history.setAiResponse(response);
        history.setModelName(config.getModelName());
        history.setTemperature(config.getTemperature());
        chatHistoryRepository.save(history);
        logger.debug("Chat history saved successfully");

        return response;
    }

    public Page<ChatHistory> getChatHistory(User user, Pageable pageable) {
        return chatHistoryRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Transactional
    public ChatConfig updateChatConfig(User user, ChatConfig newConfig) {
        ChatConfig config = chatConfigRepository.findByUser(user)
                .orElseGet(() -> {
                    ChatConfig defaultConfig = new ChatConfig();
                    defaultConfig.setUser(user);
                    return defaultConfig;
                });

        config.setModelName(newConfig.getModelName());
        config.setTemperature(newConfig.getTemperature());
        config.setMaxTokens(newConfig.getMaxTokens());
        config.setPresencePenalty(newConfig.getPresencePenalty());
        config.setFrequencyPenalty(newConfig.getFrequencyPenalty());

        return chatConfigRepository.save(config);
    }

    private boolean containsSensitiveContent(String message) {
        // 实现简单的敏感词过滤
        List<String> sensitiveWords = List.of("暴力", "色情", "赌博", "毒品");
        return sensitiveWords.stream().anyMatch(message::contains);
    }
}