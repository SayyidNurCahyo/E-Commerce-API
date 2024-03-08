package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTransactionRequest {
    @NotBlank(message = "Transaction Date is Required")
    private String transactionDate;
    @NotBlank(message = "Customer Id is Required")
    private String customerId;
    @NotBlank(message = "Table Id is Required")
    private String tableId;
    @NotBlank(message = "Table Id is Required")
    private String transTypeId;
    private List<NewDetailRequest> transactionDetails;
}
