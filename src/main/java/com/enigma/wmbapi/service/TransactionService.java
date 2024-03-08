package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.request.SearchTransactionRequest;
import com.enigma.wmbapi.dto.request.UpdateStatusRequest;
import com.enigma.wmbapi.dto.response.TransactionResponse;
import com.enigma.wmbapi.entity.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionService {

    TransactionResponse addTransaction(NewTransactionRequest request);
    Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request);
    void updateStatus(UpdateStatusRequest request);
}
