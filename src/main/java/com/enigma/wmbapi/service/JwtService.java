package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.response.JwtClaims;

public interface JwtService {
    String generateToken();
    Boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
