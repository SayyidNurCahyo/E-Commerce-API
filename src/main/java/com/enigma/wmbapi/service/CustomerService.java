package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.UpdateCustomerRequest;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void addCustomer(Customer customer);
    CustomerResponse getCustomerById(String id);
    Page<CustomerResponse> getAllCustomer(SearchCustomerRequest request);
    CustomerResponse updateCustomer(UpdateCustomerRequest customer);
    CustomerResponse disableById(String id);
}
