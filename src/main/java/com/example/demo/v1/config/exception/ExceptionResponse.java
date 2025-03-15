package com.example.demo.v1.config.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ExceptionResponse {
    private final int resultCode;
    private final String resultMsg;

    public static ResponseEntity<ExceptionResponse> toEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .resultCode(errorCode.getResultCode())
                        .resultMsg(errorCode.getResultMsg())
                        .build());
    }
}
