package com.example.demo.v1.application.auth;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import com.example.demo.v1.infrastructure.user.UserRepository;
import com.example.demo.v1.representation.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() {
        // Given
        String userId = "sample@gmail.com";
        String password = "Qq09iu!@1238798";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "encodedIdValue";

        User user = User.create(userId, passwordEncoder.encode(password), name, idType, idValue);
        userRepository.save(user);

        // When
        UserDto.tokenResponse response = authService.login(userId, password);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getResultCode()).isEqualTo(200);
        assertThat(response.getResultMsg()).isEqualTo("OK");
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    @DisplayName("잘못된 이메일, 비밀번호로 로그인 시 예외 발생")
    void 존재하지_않는_이메일_로그인_실패() {
        // Given
        String userId = "sample@gmail.com";
        String password = "Qq09iu!@1238798";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "encodedIdValue";

        User user = User.create(userId, password, name, idType, idValue);
        userRepository.save(user);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.login("wrongEmail", "password123"));
        assertThrows(RuntimeException.class, () -> authService.login("sample@gmail.com", "wrongPassword"));
    }
}
