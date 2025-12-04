package com.example.chatbothexagonal.auth.infrastructure.adapter.out.jwt;

import com.example.chatbothexagonal.auth.application.port.out.TokenEncoderPort;
import com.example.chatbothexagonal.auth.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenEncoderAdapter implements TokenEncoderPort {

    private final SecretKey key;
    private final long accessSeconds;

    public JwtTokenEncoderAdapter(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes());
        this.accessSeconds = props.getAccessMinutes() * 60L;
    }

    @Override
    public String generateAccessToken(Long userId, String name, String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(email)
                .claim("uid", userId)
                .claim("name", name)
                .claim("email", email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public long accessTokenExpiresInSeconds() {
        return accessSeconds;
    }
}
