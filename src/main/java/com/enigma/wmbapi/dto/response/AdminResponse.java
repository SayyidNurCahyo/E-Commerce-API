package com.enigma.wmbapi.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
    private String adminId;
    private String adminName;
    private String adminMobilePhone;
    private String adminUsername;
    private String adminPassword;
    private List<String> adminRole;
}
