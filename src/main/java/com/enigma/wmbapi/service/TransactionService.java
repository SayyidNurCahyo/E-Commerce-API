package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.request.SearchTransactionRequest;
import com.enigma.wmbapi.dto.request.UpdateStatusRequest;
import com.enigma.wmbapi.dto.response.GetTransactionResponse;
import com.enigma.wmbapi.dto.response.TransactionResponse;
import com.enigma.wmbapi.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    TransactionResponse addTransaction(NewTransactionRequest request);
    Page<GetTransactionResponse> getAllTransaction(SearchTransactionRequest request);
    List<GetTransactionResponse> getAllByCustomerId(String customerId);
    void updateStatus(UpdateStatusRequest request);
    List<GetTransactionResponse> getTransaction();
}
