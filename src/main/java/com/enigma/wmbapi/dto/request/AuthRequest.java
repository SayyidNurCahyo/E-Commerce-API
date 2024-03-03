package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "Password at Least 8 Character, 1 Uppercase-Lowercase-Number, And Can Contain Special Character")
    private String password;
}
