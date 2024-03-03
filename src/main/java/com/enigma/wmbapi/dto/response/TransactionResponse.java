package com.enigma.wmbapi.dto.response;

import com.enigma.wmbapi.entity.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionId;
    private Date transactionDate;
    private String customerName;
    private String customerPhone;
    private String table;
    private String transactionType;
    private List<TransactionDetailResponse> transactionDetails;
}
