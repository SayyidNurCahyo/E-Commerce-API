package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "Password at Least 8 Character, 1 Uppercase-Lowercase-Number, And Can Contain Special Character")
    private String password;
    @NotBlank(message = "Name is Required")
    private String name;
    @NotBlank(message = "Phone Number is Required")
    @Pattern(regexp = "^(\\+62|0)(8[0-9]{2}[-.\\s]?[0-9]{3,}-?[0-9]{3,}|\\(0[0-9]{2}\\)[-\\s]?[0-9]{3,}-?[0-9]{3,}|\\+62[-\\s]?[0-9]{1,2}[-.\\s]?[0-9]{3,}-?[0-9]{3,})$", message = "Phone Number Isn't In Indonesia Format")
    private String phone;
}
