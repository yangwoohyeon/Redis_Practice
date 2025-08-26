package com.example.RedisPractice.api.members.repository.redis;

import com.example.RedisPractice.api.members.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<RefreshToken, Long> {
}
