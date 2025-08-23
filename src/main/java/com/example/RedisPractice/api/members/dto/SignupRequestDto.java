package com.example.RedisPractice.api.members.dto;

import com.example.RedisPractice.api.members.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입니다.")
    private String nickname;

    //DTO -> Member 엔티티로 변환
    public Member toEntity(String encodedPassword){
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
