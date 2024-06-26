package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewTransactionRequest;
import com.enigma.wmbapi.dto.request.SearchTransactionRequest;
import com.enigma.wmbapi.dto.request.UpdateStatusRequest;
import com.enigma.wmbapi.dto.response.*;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.security.AuthenticatedUser;
import com.enigma.wmbapi.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionalController {
    private final TransactionService transactionService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<TransactionResponse>> addTransaction(@RequestBody NewTransactionRequest request){
        TransactionResponse transaction = transactionService.addTransaction(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transaction).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<GetTransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder().page(page).size(size).build();
        Page<GetTransactionResponse> transactions = transactionService.getAllTransaction(request);
        PagingResponse paging = PagingResponse.builder()
                .page(transactions.getPageable().getPageNumber() + 1)
                .size(transactions.getPageable().getPageSize())
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious()).build();
        CommonResponse<List<GetTransactionResponse>> response = CommonResponse.<List<GetTransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions.getContent())
                .paging(paging).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') or @authenticatedUser.hasId(#customerId)")
    @GetMapping(path = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<GetTransactionResponse>>> getAllTransactionByCustomerId(
            @PathVariable String customerId
    ) {
        List<GetTransactionResponse> transactions = transactionService.getAllByCustomerId(customerId);
        CommonResponse<List<GetTransactionResponse>> response = CommonResponse.<List<GetTransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions)
                .build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatus(@RequestBody Map<String, Object> request){
        UpdateStatusRequest statusRequest = UpdateStatusRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString()).build();
        transactionService.updateStatus(statusRequest);
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA).build());
    }
}
