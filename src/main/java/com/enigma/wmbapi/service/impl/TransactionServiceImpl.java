package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.request.SearchTransactionRequest;
import com.enigma.wmbapi.dto.request.UpdateStatusRequest;
import com.enigma.wmbapi.dto.response.*;
import com.enigma.wmbapi.entity.*;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final TableService tableService;
    private final TransTypeService transTypeService;
    private final MenuService menuService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransaction(NewTransactionRequest request) {
        validationUtil.validate(request);
        CustomerResponse customerResponse = customerService.getCustomerById(request.getCustomerId());
        Transaction transaction = Transaction.builder()
                .transDate(DateUtil.parseDate(request.getTransactionDate(), "yyyy-MM-dd"))
                .customer(Customer.builder().id(customerResponse.getCustomerId())
                        .name(customerResponse.getCustomerName())
                        .phone(customerResponse.getCustomerPhone())
                        .userAccount((UserAccount) userService.loadUserByUsername(customerResponse.getCustomerUsername())).build()).build();
        if (request.getTableId()!=null && !request.getTableId().isEmpty()) {
            TableResponse tableResponse = tableService.getTableById(request.getTableId());
            transaction.setTable(Table.builder().id(tableResponse.getTableId()).name(tableResponse.getTableName()).build());
            transaction.setTransType(transTypeService.getTransTypeOrSave(TransTypeId.EI,"Eat In"));
        } else {
            transaction.setTransType(transTypeService.getTransTypeOrSave(TransTypeId.TA,"Take Away"));
        }
        List<TransactionDetail> transactionDetail = request.getTransactionDetails().stream().map(detail -> {
            MenuResponse menuResponse = menuService.getMenuById(detail.getMenuId());
            return TransactionDetail.builder().transaction(transaction)
                    .menu(Menu.builder().id(menuResponse.getMenuId())
                            .name(menuResponse.getMenuName())
                            .price(menuResponse.getMenuPrice())
                            .images(menuResponse.getImageResponses().stream().map(imageResponse ->
                                    imageService.getByName(imageResponse.getName())).toList()).build())
                    .qty(detail.getMenuQuantity())
                    .price(menuResponse.getMenuPrice())
                    .build();
        }).toList();
        transaction.setTransactionDetails(transactionDetail);
        Payment payment = paymentService.createPayment(transaction);
        transaction.setPayment(payment);
        PaymentResponse paymentResponse = PaymentResponse.builder().id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectURL())
                .transactionStatus(payment.getTransactionStatus()).build();
        Transaction trSaved = transactionRepository.saveAndFlush(transaction);
        TransactionResponse response = TransactionResponse.builder().transactionId(trSaved.getId())
                .transactionDate(trSaved.getTransDate().toString()).customerName(trSaved.getCustomer().getName())
                .customerPhone(trSaved.getCustomer().getPhone())
                .transactionType(trSaved.getTransType().getDescription())
                .transactionDetails(trSaved.getTransactionDetails().stream().map(detail -> (TransactionDetailResponse.builder()
                        .detailId(detail.getId()).menu(detail.getMenu().getName())
                        .menuQuantity(detail.getQty()).menuPrice(detail.getPrice()).build())).toList())
                .paymentResponse(paymentResponse).build();
        if (trSaved.getTable()!=null) response.setTable(trSaved.getTable().getName());
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetTransactionResponse> getTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(transaction -> {
            List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream().map(detail ->
                    TransactionDetailResponse.builder()
                            .detailId(detail.getId())
                            .menu(detail.getMenu().getName())
                            .menuQuantity(detail.getQty())
                            .menuPrice(detail.getPrice())
                            .build()).toList();
            String table;
            if (transaction.getTable()!=null) table = transaction.getTable().getName();
            else table=null;
            return GetTransactionResponse.builder()
                    .transactionId(transaction.getId())
                    .transactionDate(transaction.getTransDate().toString())
                    .customerName(transaction.getCustomer().getName())
                    .customerPhone(transaction.getCustomer().getPhone())
                    .table(table)
                    .transactionType(transaction.getTransType().getDescription())
                    .transactionDetails(transactionDetailResponses)
                    .paymentResponse(PaymentResponse.builder()
                            .id(transaction.getPayment().getId())
                            .token(transaction.getPayment().getToken())
                            .redirectUrl(transaction.getPayment().getRedirectURL())
                            .transactionStatus(transaction.getPayment().getTransactionStatus()).build())
                    .build();
        }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<GetTransactionResponse> getAllTransaction(SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize());
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return transactions.map(trx -> {
            List<TransactionDetailResponse> trxDetailResponses = trx.getTransactionDetails().stream().map(detail ->
                    TransactionDetailResponse.builder()
                    .detailId(detail.getId())
                    .menu(detail.getMenu().getName())
                    .menuQuantity(detail.getQty())
                    .menuPrice(detail.getPrice())
                    .build()).toList();
            String table;
            if (trx.getTable()!=null) table = trx.getTable().getName();
            else table=null;
            return GetTransactionResponse.builder()
                    .transactionId(trx.getId())
                    .transactionDate(trx.getTransDate().toString())
                    .customerName(trx.getCustomer().getName())
                    .customerPhone(trx.getCustomer().getPhone())
                    .table(table)
                    .transactionType(trx.getTransType().getDescription())
                    .transactionDetails(trxDetailResponses)
                    .paymentResponse(PaymentResponse.builder()
                            .id(trx.getPayment().getId())
                            .token(trx.getPayment().getToken())
                            .redirectUrl(trx.getPayment().getRedirectURL())
                            .transactionStatus(trx.getPayment().getTransactionStatus()).build())
                    .build();
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetTransactionResponse> getAllByCustomerId(String customerId) {
        List<Transaction> transactions = transactionRepository.findTr(customerId);

        return transactions.stream().map(trx -> {
            List<TransactionDetailResponse> trxDetailResponses = trx.getTransactionDetails().stream().map(detail ->
                    TransactionDetailResponse.builder()
                            .detailId(detail.getId())
                            .menu(detail.getMenu().getName())
                            .menuQuantity(detail.getQty())
                            .menuPrice(detail.getPrice())
                            .build()).toList();
            String table;
            if (trx.getTable()!=null) table = trx.getTable().getName();
            else table=null;
            return GetTransactionResponse.builder()
                    .transactionId(trx.getId())
                    .transactionDate(trx.getTransDate().toString())
                    .customerName(trx.getCustomer().getName())
                    .customerPhone(trx.getCustomer().getPhone())
                    .table(table)
                    .transactionType(trx.getTransType().getDescription())
                    .transactionDetails(trxDetailResponses)
                    .paymentResponse(PaymentResponse.builder()
                            .id(trx.getPayment().getId())
                            .token(trx.getPayment().getToken())
                            .redirectUrl(trx.getPayment().getRedirectURL())
                            .transactionStatus(trx.getPayment().getTransactionStatus()).build())
                    .build();
        }).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateStatusRequest request) {
        Payment payment = paymentService.findById(request.getOrderId());
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
