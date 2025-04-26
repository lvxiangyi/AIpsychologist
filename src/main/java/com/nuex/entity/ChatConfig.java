package com.nuex.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_configs")
public class ChatConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String modelName = "gpt-3.5-turbo";
    
    @Column
    private Double temperature = 0.7;
    
    @Column(name = "max_tokens")
    private Integer maxTokens = 2000;
    
    @Column(name = "presence_penalty")
    private Double presencePenalty = 0.0;
    
    @Column(name = "frequency_penalty")
    private Double frequencyPenalty = 0.0;
}