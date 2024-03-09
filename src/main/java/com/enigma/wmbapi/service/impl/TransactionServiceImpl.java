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
        TableResponse tableResponse=null;
        if (request.getTableId()!=null) {
            tableResponse = tableService.getTableById(request.getTableId());
            transaction.setTable(Table.builder().id(tableResponse.getTableId()).name(tableResponse.getTableName()).build());
            transaction.setTransType(TransType.builder().id(TransTypeId.EI).description("Eat In").build());
        } else {
            transaction.setTransType(TransType.builder().id(TransTypeId.TA).description("Take Away").build());
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
        Transaction trSaved = transactionRepository.save(transaction);
        return TransactionResponse.builder().transactionId(trSaved.getId())
                .transactionDate(trSaved.getTransDate()).customerName(trSaved.getCustomer().getName())
                .customerPhone(trSaved.getCustomer().getPhone())
                .transactionType(trSaved.getTransType().getDescription())
                .table(trSaved.getTable().getName())
                .transactionDetails(trSaved.getTransactionDetails().stream().map(detail -> (TransactionDetailResponse.builder()
                        .detailId(detail.getId()).menu(detail.getMenu().getName())
                        .menuQuantity(detail.getQty()).menuPrice(detail.getPrice()).build())).toList())
                .paymentResponse(paymentResponse).build();
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

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllByCustomerId(String customerId, SearchTransactionRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Transaction> transactions = transactionRepository.findAllByCustomerId(customerId, pageable).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));

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
        Transaction transaction = transactionRepository.findById(request.getOrderId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        Payment payment = transaction.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
