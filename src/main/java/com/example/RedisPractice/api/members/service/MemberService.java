package com.example.RedisPractice.api.members.service;

import com.example.RedisPractice.api.members.dto.LoginRequestDto;
import com.example.RedisPractice.api.members.dto.SignupRequestDto;
import com.example.RedisPractice.api.members.entity.Member;
import com.example.RedisPractice.api.members.jwt.JwtTokenProvider;
import com.example.RedisPractice.api.members.jwt.RefreshToken;
import com.example.RedisPractice.api.members.repository.MemberRepository;
import com.example.RedisPractice.api.members.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;
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
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        // 리프레시 토큰 저장
        jwtTokenProvider.saveRefreshToken(member.getId(), refreshToken, 7 * 24 * 60 * 60L); // 7일 만료

        Map<String, Object> response = new HashMap<>();
        response.put("token",token);
        response.put("refreshToken", refreshToken);
        response.put("role",member.getRole());

        return response;
    }

    public String reissueAccessToken(String refreshToken){
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new BaseException(ErrorStatus.UNAUTHORIZED_REFRESH_TOKEN_EXPIRED.getHttpStatus(),ErrorStatus.UNAUTHORIZED_EMPTY_TOKEN.getMessage());
        }

        String email = jwtTokenProvider.getEmail(refreshToken);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new UnauthorizedException("회원 정보를 찾을 수 없습니다."));

        //Redis에서 회원 ID로 저장된 리프레시 토큰 조회
        RefreshToken storedRefreshToken = tokenRepository.findById(member.getId())
                .orElseThrow(()-> new UnauthorizedException("리프레시 토큰을 찾을 수 없습니다."));

        // 전달받은 리프레시 토큰과 Redis에서 조회한 토큰 일치 여부 확인
        if(!storedRefreshToken.getRefreshToken().equals(refreshToken)){
            throw new UnauthorizedException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 새로운 액세스 토큰 생성하여 반환
        return jwtTokenProvider.generateToken(email,member.getRole());

    }

}
