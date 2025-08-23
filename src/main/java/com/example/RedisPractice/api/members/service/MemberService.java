package com.example.RedisPractice.api.members.service;

import com.example.RedisPractice.api.members.dto.LoginRequestDto;
import com.example.RedisPractice.api.members.dto.SignupRequestDto;
import com.example.RedisPractice.api.members.entity.Member;
import com.example.RedisPractice.api.members.jwt.JwtTokenProvider;
import com.example.RedisPractice.api.members.repository.MemberRepository;
import com.example.RedisPractice.common.exception.BaseException;
import com.example.RedisPractice.common.exception.UnauthorizedException;
import com.example.RedisPractice.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.RedisPractice.api.members.entity.Role.ROLE_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpMember(SignupRequestDto signupRequestDto){
        if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()){
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getHttpStatus(),ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getMessage());
        }
        if(memberRepository.findByNickname(signupRequestDto.getNickname()).isPresent()){
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getHttpStatus(),ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        Member member = signupRequestDto.toEntity(encodedPassword);
        member.setRole(ROLE_MEMBER);
        memberRepository.save(member);
    }

    @Transactional
    public Map<String, Object> loginMember(LoginRequestDto loginRequestDto){

        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage()));

        if(!passwordEncoder.matches(loginRequestDto.getPw(),member.getPassword())){
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage());
        }

        String token = jwtTokenProvider.generateToken(member.getEmail(),member.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token",token);
        response.put("role",member.getRole());

        return response;
    }
}
