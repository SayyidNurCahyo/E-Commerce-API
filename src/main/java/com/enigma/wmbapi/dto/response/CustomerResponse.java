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
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerUsername;
    private String customerPassword;
    private List<String> customerRole;
}
