package com.example.demo.v1.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 4xx Client Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    QUOTE_EXPIRED(HttpStatus.BAD_REQUEST, 400, "만료된 견적서입니다."),
    QUOTE_USED(HttpStatus.BAD_REQUEST, 400, "이미 사용된 견적서입니다."),
    NOT_SUPPORTED_CURRENCY(HttpStatus.BAD_REQUEST, 400, "지원하지 않는 통화입니다."),
    NEGATIVE_NUMBER(HttpStatus.BAD_REQUEST, 400, "값이 음수가 될 수 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "접근 할수없습니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "데이터를 찾을 수 없습니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "허용되지 않은 메소드 입니다."),

    // 5xx Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "내부 서버 오류."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "외부 API 연동 실패"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "알 수 없는 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final int resultCode;
    private final String resultMsg;
}

