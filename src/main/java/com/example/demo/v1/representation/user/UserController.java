package com.example.demo.v1.representation.user;

import com.example.demo.v1.application.auth.AuthService;
import com.example.demo.v1.application.user.UserFacade;
import com.example.demo.v1.config.response.CommonResponse;
import com.example.demo.v1.representation.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원 관련 API")
public class UserController {
    private final UserFacade userFacade;

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원 정보를 받아서 가입을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    public CommonResponse<Void> signup(@RequestBody UserDto.UserCreateRequest userRequest) {
        userFacade.signup(userRequest.toEntity());
        return CommonResponse.success();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserDto.tokenResponse.class)))
    @ApiResponse(responseCode = "401", description = "인증 실패")
    public UserDto.tokenResponse login(@RequestBody UserDto.LoginRequest loginRequest) {
        return authService.login(loginRequest.getUserId(), loginRequest.getPassword());
    }
}
