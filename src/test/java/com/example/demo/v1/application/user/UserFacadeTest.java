package com.example.demo.v1.application.user;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@Transactional
class UserFacadeTest {
    @Autowired
    private UserFacade userFacade;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @Order(1)
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공() {
        // Given
        String userId = "test@example.com";
        String password = "password";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "idValue";

        // When
        User savedUser = userFacade.signup(User.create(userId, password, name, idType, idValue));

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(userId);
        assertThat(savedUser.getIdType()).isEqualTo(idType);
    }

    @Test
    @Order(2)
    @DisplayName("중복된 이메일로 회원가입 시 예외 발생 테스트")
    void 중복된_이메일로_회원가입_시_예외발생() {
        // Given
        String userId = "test@example.com";
        String password = "password";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "idValue";

        // When
        // 첫번째 회원가입 성공
        userFacade.signup(User.create(userId, password, name, idType, idValue));

        // then
        // 존재하는 이메일로 가입시 Runtime Exception
        assertThrows(RuntimeException.class, () -> {
            userFacade.signup(User.create(userId, password, name, idType, idValue));
        });
    }

    @Test
    @Order(3)
    @DisplayName("패스워드 및 idValue 인코딩 테스트")
    void 패스워드_및_idValue_인코딩_확인() {
        // Given
        String userId = "test@example.com";
        String password = "password";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "idValue";

        // When
        User savedUser = userFacade.signup(User.create(userId, password, name, idType, idValue));

        // Then
        assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(idValue, savedUser.getIdValue())).isTrue();
    }
}