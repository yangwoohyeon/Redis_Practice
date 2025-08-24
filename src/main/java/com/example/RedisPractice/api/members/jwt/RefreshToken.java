package com.example.RedisPractice.api.members.jwt;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "token")
@AllArgsConstructor
@Getter
@ToString
public class RefreshToken {

    @Id
    private Long id; //Member ID와 매핑

    private String refreshToken;

    @TimeToLive
    private Long expiration;
}
