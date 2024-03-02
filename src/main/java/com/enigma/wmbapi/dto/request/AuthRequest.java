package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username is Required")
    private String username;
    @NotBlank(message = "Password is Required")
    private String password;
}
