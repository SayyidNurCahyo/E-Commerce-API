package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Username is Required")
    private String username;
    @NotBlank(message = "Password is Required")
    private String password;
    @NotBlank(message = "Name is Required")
    private String name;
    @NotBlank(message = "Mobile Phone Number is Required")
    private String phone;
}
