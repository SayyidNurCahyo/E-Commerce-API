package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.request.SearchTransactionRequest;
import com.enigma.wmbapi.dto.request.UpdateStatusRequest;
import com.enigma.wmbapi.dto.response.TableResponse;
import com.enigma.wmbapi.dto.response.TransactionDetailResponse;
import com.enigma.wmbapi.dto.response.TransactionResponse;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.entity.TransactionDetail;
import com.enigma.wmbapi.repository.*;
import com.enigma.wmbapi.service.*;
import com.enigma.wmbapi.util.DateUtil;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransaction(NewTransactionRequest request) {
        validationUtil.validate(request);
        Transaction transaction = Transaction.builder()
                .transDate(DateUtil.parseDate(request.getTransactionDate(), "yyyy-MM-dd"))
                .customer(customerService.getCustomerById(request.getCustomerId()))
                .table(tableService.getTableById(request.getTableId()))
                .transType(transTypeService.getTransTypeById(Enum.valueOf(TransTypeId.class,request.getTransTypeId()))).build();
        List<TransactionDetail> transactionDetail = request.getTransactionDetails().stream().map(detail -> (
            TransactionDetail.builder().transaction(transaction)
                    .menu(menuService.getMenuById(detail.getMenuId()))
                    .qty(detail.getMenuQuantity())
                    .price(menuService.getMenuById(detail.getMenuId()).getPrice().floatValue())
                    .build()
        )).toList();
        transaction.setTransactionDetails(transactionDetail);
        Transaction trSaved = transactionRepository.save(transaction);
        return TransactionResponse.builder().transactionId(trSaved.getId())
                .transactionDate(trSaved.getTransDate()).customerName(trSaved.getCustomer().getName())
                .customerPhone(trSaved.getCustomer().getPhone())
                .transactionType(trSaved.getTransType().getDescription())
                .table(trSaved.getTable().getName())
                .transactionDetails(trSaved.getTransactionDetails().stream().map(detail -> (TransactionDetailResponse.builder()
                        .detailId(detail.getId()).menu(detail.getMenu().getName())
                        .menuQuantity(detail.getQty()).menuPrice(detail.getPrice()).build())).toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return transactions.map(trx -> {
            List<TransactionDetailResponse> trxDetailResponses = trx.getTransactionDetails().stream().map(detail ->
                    TransactionDetailResponse.builder()
                    .detailId(detail.getId())
                    .menu(detail.getMenu().getName())
                    .menuQuantity(detail.getQty())
                    .menuPrice(detail.getPrice())
                    .build()).toList();

            return TransactionResponse.builder()
                    .transactionId(trx.getId())
                    .transactionDate(trx.getTransDate())
                    .customerName(trx.getCustomer().getName())
                    .customerPhone(trx.getCustomer().getPhone())
                    .table(trx.getTable().getName())
                    .transactionType(trx.getTransType().getDescription())
                    .transactionDetails(trxDetailResponses)
                    .build();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateStatusRequest request) {

    }
}
