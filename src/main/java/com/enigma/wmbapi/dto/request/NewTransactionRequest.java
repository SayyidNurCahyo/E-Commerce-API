package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String tableId;
    @NotNull(message = "Transaction Detail is Required")
    private List<NewDetailRequest> transactionDetails;
}
