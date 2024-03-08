package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.AuthRequest;
import com.enigma.wmbapi.dto.request.RegisterRequest;
import com.enigma.wmbapi.dto.response.LoginResponse;
import com.enigma.wmbapi.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
