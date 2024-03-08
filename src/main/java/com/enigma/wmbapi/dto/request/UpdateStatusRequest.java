package com.enigma.wmbapi.dto.request;

import jdk.jfr.Name;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusRequest {
    private String orderId;
    private String transactionStatus;
}
