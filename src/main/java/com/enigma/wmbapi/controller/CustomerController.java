package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.enigma.wmbapi.dto.response.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer customerAdded = customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerAdded);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Customer>>> getAllCustomer(
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
        Page<Customer> all = customerService.getAll(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(all.getTotalPages())
                .totalElement(all.getTotalElements())
                .page(all.getPageable().getPageNumber()+1)
                .size(all.getPageable().getPageSize())
                .hasNext(all.hasNext())
                .hasPrevious(all.hasPrevious()).build();
        CommonResponse<List<Customer>> response = CommonResponse.<List<Customer>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("ok")
                .data(all.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer product) {
        Customer update = customerService.update(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(update);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateStatusCustomer(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ) {
        Customer customer = customerService.updateStatusById(id, status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteById(@PathVariable String id) {
        Customer customer = customerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
