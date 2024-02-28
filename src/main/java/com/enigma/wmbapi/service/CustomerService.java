package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<Customer> addCustomer(Customer customer);
    ResponseEntity<Customer> getCustomerById(String id);
    ResponseEntity<CommonResponse<List<Customer>>> getAllCustomer(Integer page, Integer size, String sortBy, String direction, String name, String phone);
    ResponseEntity<Customer> updateCustomer(Customer customer);
    ResponseEntity<Customer> deleteById(String id);
}
