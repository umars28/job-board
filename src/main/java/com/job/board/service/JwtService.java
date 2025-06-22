package com.job.board.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;

    public JwtService() {
        this.algorithm = Algorithm.HMAC256("MySuperSecretKey1234567890");
    }

    public String generateToken(Long userId, String role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(86400); // 1 hari

        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("role", role)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }
}

