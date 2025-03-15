package com.example.demo.v1.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;          // email

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdType idType;

    @Column(nullable = false)
    private String idValue;

    public static User create(String userId, String password, String name, IdType idType, String idValue) {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .idType(idType)
                .idValue(idValue)
                .build();
    }

    public void encryptValues(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        this.idValue = passwordEncoder.encode(this.idValue);
    }
}
