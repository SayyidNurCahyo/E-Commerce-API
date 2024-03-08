package com.enigma.wmbapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.response.JwtClaims;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;
    private final Long JWT_EXPIRATION;
    public JwtServiceImpl(@Value("${wmbapi.jwt.secret_key}") String jwtSecret,
                          @Value("${wmbapi.jwt.issuer}") String issuer,
                          @Value("${wmbapi.jwt.expirationInSecond}") Long jwtExpiration){
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
        JWT_EXPIRATION = jwtExpiration;
    }

    @Override
    public String generateToken(UserAccount account) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create().withSubject(account.getId())
                    .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withIssuer(ISSUER).sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.ERROR_CREATING_JWT);
        }
    }

    @Override
    public Boolean verifyJwtToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            jwtVerifier.verify(parseJwt(bearerToken));
            return true;
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parseJwt(bearerToken));
            return JwtClaims.builder().userAccountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class)).build();
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return null;
        }
    }

    private String parseJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
