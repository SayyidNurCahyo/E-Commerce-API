package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.response.JwtClaims;
import com.enigma.wmbapi.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    Boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
