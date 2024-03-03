package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.response.TableResponse;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.entity.TransactionDetail;
import com.enigma.wmbapi.repository.*;
import com.enigma.wmbapi.service.*;
import com.enigma.wmbapi.util.DateUtil;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final TableService tableService;
    private final TransTypeService transTypeService;
    private final TransactionDetailService detailService;
    private final MenuService menuService;

    @Override
    public Transaction addTransaction(NewTransactionRequest request) {
        validationUtil.validate(request);
        Transaction transaction = Transaction.builder()
                .transDate(DateUtil.parseDate(request.getTransactionDate(), "yyyy-MM-dd"))
                .customer(customerService.getCustomerById(request.getCustomerId()))
                .table(tableService.getTableById(request.getTableId()))
                .transType(transTypeService.getTransTypeById(request.getTransTypeId())).build();
        List<TransactionDetail> transactionDetail = transaction.getTransactionDetails().stream().map(detail -> {
            return TransactionDetail.builder().transaction(transaction)
                    .menu(menuService.getMenuById(detail.getMenu().getId()))
                    .qty(detail.getQty())
                    .price(menuService.getMenuById(detail.getMenu().getId()).getPrice().floatValue())
                    .build();
        }).toList();
        transaction.setTransactionDetails(transactionDetail);
        return transactionRepository.save(transaction);
    }
}
