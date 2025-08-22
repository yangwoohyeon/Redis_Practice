package com.example.RedisPractice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) //Json 변환 시 null인 필드는 응답에서 제외
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private T data;

    public static <T>ResponseEntity<ApiResponse<T>> success(SuccessStatus status, T data){
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(status.getStatusCode()) //상태코드
                .success(true) //성공 응답
                .message(status.getMessage()) //SuccessStatus에서 상황에 맞는 응답 메시지 사용
                .data(data) //실제 응답 데이터
                .build();
        return ResponseEntity.status(status.getStatusCode()).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> successOnly(SuccessStatus status) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(status.getStatusCode())
                .success(true)
                .message(status.getMessage())
                .build();
        return ResponseEntity.status(status.getStatusCode()).body(response);
    }

    public static ApiResponse<Void> fail(int status, String message) {
        return ApiResponse.<Void>builder()
                .status(status)
                .success(false)
                .message(message)
                .build();
    }

    public static ApiResponse<Void> failOnly(ErrorStatus status) {
        return ApiResponse.<Void>builder()
                .status(status.getStatusCode())
                .success(false)
                .message(status.getMessage())
                .build();
    }
}
