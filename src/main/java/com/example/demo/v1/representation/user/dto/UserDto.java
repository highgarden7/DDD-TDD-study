package com.example.demo.v1.representation.user.dto;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class UserDto {
    @Data
    @Builder
    @Schema(description = "회원가입 요청 DTO")
    public static class UserCreateRequest {
        @Schema(description = "사용자 ID (이메일 형식)", example = "sample@gmail.com")
        private String userId;

        @Schema(description = "비밀번호", example = "Qq09iu!@1238798")
        private String password;

        @Schema(description = "사용자 이름", example = "테스")
        private String name;

        @Schema(description = "ID 유형 (REG_NO | BUSINESS_NO)", example = "REG_NO")
        private IdType idType;

        @Schema(description = "ID 값 (주민등록번호 또는 사업자등록번호)", example = "001123-3111111")
        private String idValue;

        public User toEntity() {
            return User.create(
                    this.userId,
                    this.password,
                    this.name,
                    this.idType,
                    this.idValue);
        }
    }

    @Data
    public static class LoginRequest {
        private String userId;
        private String password;
    }

    @Getter
    @Builder
    public static class tokenResponse {
        private int resultCode;
        private String resultMsg;
        private String token;

        public static tokenResponse of(String token) {
            return tokenResponse.builder()
                    .resultCode(200)
                    .resultMsg("OK")
                    .token(token)
                    .build();
        }
    }
}
