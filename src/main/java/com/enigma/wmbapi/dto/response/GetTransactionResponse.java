package com.enigma.wmbapi.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTransactionResponse {
    private String transactionId;
    private String transactionDate;
    private String customerName;
    private String customerPhone;
    private String table;
    private String transactionType;
    private List<TransactionDetailResponse> transactionDetails;
    private String transactionStatus;
}
