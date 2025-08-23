package com.example.RedisPractice.api.members.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {

    @NotBlank(message="리프레시 토큰은 필수 입니다.")
    private String refreshToken;

}
