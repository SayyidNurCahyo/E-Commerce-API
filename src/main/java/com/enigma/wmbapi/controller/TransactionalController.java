package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.TransactionResponse;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionalController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>>
}
