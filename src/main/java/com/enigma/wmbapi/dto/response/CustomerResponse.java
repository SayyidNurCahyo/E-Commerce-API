package com.enigma.wmbapi.dto.response;

import com.enigma.wmbapi.constant.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String phone;
    private String username;
    private String password;
    private List<String> role;
}
