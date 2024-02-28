package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    Customer getCustomerById(String id);
    Page<Customer> getAllCustomer(SearchCustomerRequest request);
    Customer updateCustomer(Customer customer);
    Customer deleteById(String id);
}
