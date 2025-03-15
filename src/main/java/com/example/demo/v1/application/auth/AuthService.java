package com.example.demo.v1.application.auth;

import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.config.jwt.JwtProvider;
import com.example.demo.v1.domain.user.User;
import com.example.demo.v1.infrastructure.user.UserRepository;
import com.example.demo.v1.representation.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public UserDto.tokenResponse login(String userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = jwtProvider.generateToken(user);
        return UserDto.tokenResponse.of(token);
    }
}
