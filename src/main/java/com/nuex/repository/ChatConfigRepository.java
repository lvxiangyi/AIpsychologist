package com.nuex.repository;

import com.nuex.entity.ChatConfig;
import com.nuex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatConfigRepository extends JpaRepository<ChatConfig, Long> {
    Optional<ChatConfig> findByUser(User user);
}