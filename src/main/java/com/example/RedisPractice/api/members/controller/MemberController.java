package com.example.RedisPractice.api.members.controller;


import com.example.RedisPractice.api.members.dto.SignupRequestDto;
import com.example.RedisPractice.api.members.service.MemberService;
import com.example.RedisPractice.common.response.ApiResponse;
import com.example.RedisPractice.common.response.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> sign(@Valid @RequestBody SignupRequestDto signupRequestDto){
        memberService.signUpMember(signupRequestDto);
        return ApiResponse.successOnly(SuccessStatus.SIGNUP_SUCCESS);
    }
}
