package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.UpdateCustomerRequest;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import com.enigma.wmbapi.dto.response.CommonResponse;

import java.util.Currency;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerResponse customerResponse;
        if(customer.getUserAccount()==null){
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId()).customerName(customer.getName())
                    .customerMobilePhone(customer.getPhone()).build();
        }else {
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId())
                    .customerName(customer.getName())
                    .customerMobilePhone(customer.getPhone())
                    .customerUsername(customer.getUserAccount().getUsername())
                    .customerPassword(customer.getUserAccount().getPassword())
                    .customerRole(customer.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .build();
        }
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Customer Exists")
                .data(customerResponse).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).phone(phone).build();
        Page<Customer> customers = customerService.getAllCustomer(request);
        Page<CustomerResponse> customerResponses = customers.map(customer -> customer.getUserAccount() == null ?
                        CustomerResponse.builder()
                                .customerId(customer.getId()).customerName(customer.getName())
                                .customerMobilePhone(customer.getPhone()).build() :
                        CustomerResponse.builder()
                                .customerId(customer.getId()).customerName(customer.getName())
                                .customerMobilePhone(customer.getPhone())
                                .customerUsername(customer.getUserAccount().getUsername())
                                .customerPassword(customer.getUserAccount().getPassword())
                                .customerRole(customer.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                                .build());
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(customers.getTotalPages())
                .totalElement(customers.getTotalElements())
                .page(customers.getPageable().getPageNumber()+1)
                .size(customers.getPageable().getPageSize())
                .hasNext(customers.hasNext())
                .hasPrevious(customers.hasPrevious()).build();
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Customer Exists")
                .data(customerResponses.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody UpdateCustomerRequest request) {
        Customer customer = customerService.updateCustomer(request);
        CustomerResponse customerResponse;
        if(customer.getUserAccount()==null){
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId()).customerName(customer.getName()).customerMobilePhone(customer.getPhone()).build();
        }else {
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId())
                    .customerName(customer.getName())
                    .customerMobilePhone(customer.getPhone())
                    .customerUsername(customer.getUserAccount().getUsername())
                    .customerPassword(customer.getUserAccount().getPassword())
                    .customerRole(customer.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .build();
        }
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Data Customer Updated")
                .data(customerResponse).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteById(@PathVariable String id) {
        Customer customer = customerService.deleteById(id);
        CustomerResponse customerResponse;
        if(customer.getUserAccount()==null){
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId()).customerName(customer.getName()).customerMobilePhone(customer.getPhone()).build();
        }else {
            customerResponse = CustomerResponse.builder()
                    .customerId(customer.getId())
                    .customerName(customer.getName())
                    .customerMobilePhone(customer.getPhone())
                    .customerUsername(customer.getUserAccount().getUsername())
                    .customerPassword(customer.getUserAccount().getPassword())
                    .customerRole(customer.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .build();
        }
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Customer Deleted")
                .data(customerResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
