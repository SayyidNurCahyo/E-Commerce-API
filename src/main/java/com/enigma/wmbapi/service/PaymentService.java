package com.enigma.wmbapi.service;

import com.enigma.wmbapi.entity.Payment;
import com.enigma.wmbapi.entity.Transaction;

public interface PaymentService {
    Payment createPayment(Transaction transaction);
    void checkFailedAndUpdateStatus();
}
