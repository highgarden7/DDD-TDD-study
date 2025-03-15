package com.example.demo.v1.application.user;

import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.domain.user.User;
import com.example.demo.v1.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User getUserById(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public boolean isExist(String userId) {
        return userRepository.existsUserByUserId(userId);
    }

    public User save(User user) {
        user.encryptValues(passwordEncoder);
        return userRepository.save(user);
    }
}
