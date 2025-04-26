package com.nuex.config;

import com.nuex.entity.User;
import com.nuex.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldLoadUserByUsername() {
        // 模拟用户数据
        User mockUser = new User();
        mockUser.setUsername("12345");
        mockUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMy..."); // BCrypt加密后的密码
        
        // 模拟Repository行为
        when(userRepository.findByUsername("12345"))
            .thenReturn(Optional.of(mockUser));

        // 测试方法
        UserDetails userDetails = userDetailsService.loadUserByUsername("12345");
        
        // 验证结果
        assertNotNull(userDetails);
        assertEquals("12345", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // 模拟用户不存在
        when(userRepository.findByUsername("nonexistent"))
            .thenReturn(Optional.empty());

        // 验证抛出异常
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }
}