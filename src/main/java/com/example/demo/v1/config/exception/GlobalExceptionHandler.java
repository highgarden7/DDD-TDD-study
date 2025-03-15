package com.example.demo.v1.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        log.warn("[MethodArgumentNotValidException] errorCode = {}, errorMessage = {}", errorCode,
                NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ExceptionResponse.toEntity(errorCode, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ExceptionResponse.toEntity(errorCode, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ExceptionResponse> handleAccessDeniedException(final AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return ExceptionResponse.toEntity(errorCode, e.getMessage());
    }

    // 사용자 정의 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        StackTraceElement firstElement = e.getStackTrace()[0];

        log.warn("[CustomException] errorCode = {}, errorMessage = {}, class = {}, method = {}, line = {}",
                errorCode,
                NestedExceptionUtils.getMostSpecificCause(e).getMessage(),
                firstElement.getClassName(), firstElement.getMethodName(), firstElement.getLineNumber(), e);

        return ExceptionResponse.toEntity(errorCode, e.getMessage());
    }

    // 기타 예외 (서버 오류)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        StackTraceElement firstElement = e.getStackTrace()[0];

        log.error("[Exception] errorMessage = {}, class = {}, method = {}, line = {}",
                e.getMessage(),
                firstElement.getClassName(), firstElement.getMethodName(), firstElement.getLineNumber(), e);

        return ExceptionResponse.toEntity(ErrorCode.UNKNOWN_ERROR, e.getMessage());
    }
}
