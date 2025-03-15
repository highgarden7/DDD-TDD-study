package com.example.demo.v1.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(int resultCode, String resultMsg, T data) {
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, "OK", data);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(200, "OK", null);
    }
}