package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTransactionRequest {
    private String customerId;
    private String tableId;
    private TransTypeId transTypeId;
    private List<NewDetailRequest> transactionDetails;
}
