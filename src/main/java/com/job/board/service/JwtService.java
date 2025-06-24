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

    public String generateToken(Long userId, String role, Long conversationId, Long jobId, String displayName) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(86400);

        return JWT.create()
                .withIssuer("job-board")
                .withAudience("chat-service")
                .withSubject(String.valueOf(userId))
                .withClaim("role", role)
                .withClaim("conversationId", conversationId)
                .withClaim("jobId", jobId)
                .withClaim("chatWith", displayName)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }
}