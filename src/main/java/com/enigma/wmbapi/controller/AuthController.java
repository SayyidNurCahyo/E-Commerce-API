package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody AuthRequest authRequest){
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message("successfully create new account")
                .data(authService.register(authRequest)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request){
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("Login Successfully")
                .data(loginResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
