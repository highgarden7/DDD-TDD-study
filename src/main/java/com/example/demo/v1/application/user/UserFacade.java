package com.example.demo.v1.application.user;

import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    @Transactional
    public User signup(User user) {
        if (userService.isExist(user.getUserId())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이미 존재하는 유저입니다.");
        }

        return userService.save(user);
    }
}
