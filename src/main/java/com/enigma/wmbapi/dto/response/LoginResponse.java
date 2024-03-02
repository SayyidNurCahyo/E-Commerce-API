package com.enigma.wmbapi.dto.response;

import com.enigma.wmbapi.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String username;
    private String token;
    private List<Role> roles;
}
