package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTransactionRequest {
    @NotBlank(message = "Transaction Date is Required")
    @Pattern(regexp = "^(?!0000)[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "Date Format Isn't In yyyy-MM-dd")
    private String transactionDate;
    @NotBlank(message = "Customer Id is Required")
    private String customerId;
    private String tableId;
    @NotNull(message = "Transaction Detail is Required")
    private List<NewDetailRequest> transactionDetails;
}
