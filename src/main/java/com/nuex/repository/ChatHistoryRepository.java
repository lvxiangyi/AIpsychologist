package com.nuex.repository;

import com.nuex.entity.ChatHistory;
import com.nuex.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    Page<ChatHistory> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<ChatHistory> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}