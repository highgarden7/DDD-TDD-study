package com.example.demo.v1.infrastructure.user;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 저장 테스트")
    void 회원_저장_및_조회_테스트() {
        // Given
        String userId = "test@example.com";
        String password = "encodedPassword";
        String name = "test";
        IdType idType = IdType.REG_NO;
        String idValue = "encodedIdValue";

        User user = User.create(userId, password, name, idType, idValue);
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUserId("test@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getPassword()).isEqualTo("encodedPassword");
        assertThat(foundUser.get().getIdType()).isEqualTo(IdType.REG_NO);
        assertThat(foundUser.get().getIdValue()).isEqualTo("encodedIdValue");
    }

    @Test
    @DisplayName("조회 테스트")
    void 조회_테스트() {
        // Given
        String userId = "test@example.com";

        User user = new User(userId, "password", "test",IdType.REG_NO, "idValue");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUserId("test@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("존재 여부 테스트")
    void 존재_여부_테스트() {
        // Given
        String userId = "test@example.com";

        User user = new User(userId, "password", "test", IdType.REG_NO, "idValue");
        userRepository.save(user);

        // When
        boolean isExist = userRepository.existsUserByUserId("test@example.com");

        // Then
        assertThat(isExist).isEqualTo(true);
    }
}
