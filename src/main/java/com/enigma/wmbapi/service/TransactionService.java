package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.entity.Transaction;

public interface TransactionService {

    Transaction addTransaction(NewTransactionRequest request);
}
