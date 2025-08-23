package com.example.RedisPractice.api.members.jwt;

import com.example.RedisPractice.api.members.entity.Role;
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

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

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
}

