package com.example.RedisPractice.api.members.jwt;

import com.example.RedisPractice.api.members.entity.Role;
import com.example.RedisPractice.api.members.repository.TokenRepository;
import com.example.RedisPractice.common.exception.UnauthorizedException;
import com.example.RedisPractice.common.response.ErrorStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

        private final TokenRepository tokenRepository;

        @Value("${jwt.secret}")
        private String secretKeyString;

        @Value("${jwt.expiration}")
        private long expiration;

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        }

        public String generateToken(String email, Role role) { //사용자 이메일과 Role정보를 포함한 JWT 토큰 생성

            Date now = new Date();
            Date expiry = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .setSubject(email) // sub
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }



        public String getEmail(String token) { //JWT 토큰에서 이메일 정보 추출
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public String getRole(String token) { //JWT 토큰에서 Role 정보 추출
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        }

        public boolean validateToken(String token) { //토큰 유효성 검사
            try {
                Jwts.parser()
                        .setSigningKey(getSigningKey())
                        .parseClaimsJws(token);
                return true;
            } catch (ExpiredJwtException e) {
                System.out.println("Expired JWT token: " + e.getMessage());
                throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_TOKEN_EXPIRED.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("Malformed JWT token: " + e.getMessage());
                throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_INVALID_TOKEN.getMessage());
            } catch (UnsupportedJwtException e) {
                System.out.println("Unsupported JWT token: " + e.getMessage());
                throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_UNSUPPORTED_TOKEN.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Empty JWT token: " + e.getMessage());
                throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMPTY_TOKEN.getMessage());
            }
        }


    public String generateRefreshToken(String email) {
        Date now = new Date();

        long refreshExpiration = 7 * 24 * 60 * 60 * 1000L; // 7일 밀리초 단위
        Date expiry = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(email) // 토큰 대상자 이메일 정보
                .setIssuedAt(now)
                .setExpiration(expiry)
                // 서명 키는 기존 액세스 토큰 서명키와 동일하게 사용
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 리프레시 토큰 저장
     * @param id 토큰의 고유 식별자 (예: 사용자 ID)
     * @param refreshToken 실제 리프레시 토큰 문자열
     * @param expirationSeconds 만료 시간(초)
     */
    public void saveRefreshToken(Long id, String refreshToken, Long expirationSeconds) {
        RefreshToken token = new RefreshToken(id, refreshToken, expirationSeconds);
        tokenRepository.save(token);
    }

    /**
     * 리프레시 토큰 조회
     * @param id 토큰 고유 식별자
     * @return RefreshToken 엔티티, 없으면 null 반환
     */
    public RefreshToken getRefreshToken(Long id) {
        Optional<RefreshToken> tokenOptional = tokenRepository.findById(id);
        return tokenOptional.orElse(null);
    }

    /**
     * 리프레시 토큰 삭제
     * @param id 토큰 고유 식별자
     */
    public void deleteRefreshToken(Long id) {
        tokenRepository.deleteById(id);
    }

}

