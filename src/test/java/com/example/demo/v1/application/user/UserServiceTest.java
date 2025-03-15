package com.example.demo.v1.application.user;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import com.example.demo.v1.infrastructure.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }


    @Test
    @Order(1)
    @DisplayName("유저 저장 테스트")
    void 유저저장_성공() {
        // Given
        String userId = "test@example.com";
        String password = "password";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "idValue";

        User user = User.create(userId, password, name, idType, idValue);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("password");
        assertThat(savedUser.getIdType()).isEqualTo(IdType.REG_NO);
        assertThat(savedUser.getIdValue()).isEqualTo("idValue");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @Order(2)
    @DisplayName("이메일로 회원 조회 테스트")
    void 이메일로_회원_조회() {
        // Given
        String userId = "test@example.com";
        String password = "password";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "idValue";

        User user = User.create(userId, password, name, idType, idValue);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getUserById(userId);

        // Then
        assertThat(foundUser.getUserId()).isEqualTo(userId);
    }

    @Test
    @Order(3)
    @DisplayName("회원 존재여부 테스트")
    void 회원_존재여부_테스트() {
        // Given
        String userId1 = "test@example.com";
        String userId2 = "test2@example.com";

        // When
        when(userRepository.existsUserByUserId(userId1)).thenReturn(true);
        when(userRepository.existsUserByUserId(userId2)).thenReturn(false);

        // then
        assertThat(userService.isExist(userId1)).isTrue();
        assertThat(userService.isExist(userId2)).isFalse();
    }
}