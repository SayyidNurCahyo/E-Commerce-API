package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.UpdateCustomerRequest;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.CustomerRepository;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final UserAccountRepository userAccountRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllCustomer(SearchCustomerRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null || request.getPhone()!=null){
            return customerRepository.findCustomer(request.getName(), request.getPhone(), page);
        }else {
            return customerRepository.findAll(page);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer updateCustomer(UpdateCustomerRequest request) {
        validationUtil.validate(request);
        Customer customer = customerRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
        customer.getUserAccount().setUsername(request.getUsername());
        Customer customerNew = Customer.builder().id(customer.getId()).name(customer.getName())
                .phone(customer.getPhone()).userAccount(customer.getUserAccount()).build();
        return customerRepository.saveAndFlush(customerNew);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer deleteById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
        customerRepository.delete(customer);
        return customer;
    }
}
