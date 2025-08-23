package com.example.RedisPractice.common.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * 200 OK
     */
    SIGNUP_SUCCESS(HttpStatus.OK,"회원가입 성공"),
    LOGIN_SUCCESS(HttpStatus.OK,"로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK,"로그아웃 성공"),

    /**
     * 201 CREATED
     */


    /**
     * 204 NO CONTENT
     */

;
    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }


}
