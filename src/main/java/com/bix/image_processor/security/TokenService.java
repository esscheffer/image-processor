package com.bix.image_processor.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${security.token.secret}")
    private String secret;

    public String generateToken(UserEntity user) {
        try {
            return JWT.create()
                    .withIssuer("image-processor")
                    .withSubject(user.getUsername())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("image-processor")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
}
