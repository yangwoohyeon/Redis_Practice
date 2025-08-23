package com.example.RedisPractice.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorStatus {
    /**
     * 400 BAD_REQUEST (요청값 문제)
     */
    BAD_REQUEST_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST,"이미 가입된 사용자의 이메일 입니다."),
    BAD_REQUEST_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"사용중인 닉네임 입니다."),


    /**
     * 401 UNAUTHORIZED (인증되지 않은 사용자의 요청)
     */
    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    UNAUTHORIZED_EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 비어있습니다."),
    /**
     * 403 FORBIDDEN (권한이 없어서 접근X)
     */

    /**
     * 404 NOT_FOUND (잘못된 URL, 요청 자원이 서버에 존재 X)
     */

    /**
     * 500 SERVER_ERROR (서버 내부 오류)
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
