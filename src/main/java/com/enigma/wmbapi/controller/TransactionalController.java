package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.dto.response.TransactionDetailResponse;
import com.enigma.wmbapi.dto.response.TransactionResponse;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionalController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> addTransaction(@RequestBody NewTransactionRequest request){
        Transaction transaction = transactionService.addTransaction(request);
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionDate(transaction.getTransDate())
                .customerName(transaction.getCustomer().getName())
                .customerPhone(transaction.getCustomer().getPhone())
                .table(transaction.getTable().getName())
                .transactionType(transaction.getTransType().getDescription())
                .transactionDetails(transaction.getTransactionDetails().stream().map(detail ->{
                    return TransactionDetailResponse.builder().detailId(detail.getId())
                            .menu(detail.getMenu().getName())
                            .menuQuantity(detail.getQty())
                            .menuPrice(detail.getPrice()).build();
                }).toList()).build();
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Transaction Added")
                .data(transactionResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
