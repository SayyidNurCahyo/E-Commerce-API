package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.UpdateCustomerRequest;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.CustomerRepository;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private UserAccountRepository userAccountRepository;
    private CustomerService customerService;
    @BeforeEach
    void setUp(){
        customerService = new CustomerServiceImpl(customerRepository, validationUtil, userAccountRepository);
    }

    @Test
    void addCustomer() {
        Customer customer = Customer.builder().id("tes id").name("cahyo").phone("088888")
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        when(customerRepository.saveAndFlush(Mockito.any(Customer.class))).thenReturn(customer);
        Customer result = customerRepository.saveAndFlush(customer);
        assertEquals(result.getUserAccount().getUsername(), customer.getUserAccount().getUsername());
    }

    @Test
    void getCustomerById() {
        String id = "tes id";
        Customer customer = Customer.builder().id("tes id").name("cahyo").phone("088888")
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        CustomerResponse result = customerService.getCustomerById(id);
        assertEquals(result.getCustomerUsername(), customer.getUserAccount().getUsername());
    }

    @Test
    void getAllCustomer() {
        SearchCustomerRequest request = SearchCustomerRequest.builder().size(5)
                .page(1).direction("asc").sortBy("name").build();
        Page<Customer> customerPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customerPage);
        Page<CustomerResponse> result = customerService.getAllCustomer(request);
        assertEquals(customerPage.getTotalElements(), result.getTotalElements());
        assertEquals(customerPage.getTotalPages(), result.getTotalPages());
        assertEquals(customerPage.getNumber(), result.getNumber());
        assertEquals(customerPage.getSize(), result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void updateCustomer() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId("tesId");
        request.setName("Cahyo");
        request.setPhone("1234567890");
        request.setUsername("cahyo123");
        Customer customer = new Customer();
        customer.setId("tesId");
        customer.setName("Cahyo2");
        customer.setPhone("0987654321");
        UserAccount account = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        account.setUsername("Cahyo234");
        customer.setUserAccount(account);
        when(customerRepository.findById(request.getId())).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.updateCustomer(request);
        assertEquals(request.getName(), response.getCustomerName());
        assertEquals(request.getPhone(), response.getCustomerPhone());
        assertEquals(request.getUsername(), response.getCustomerUsername());
    }

    @Test
    void deleteById() {
        Customer customer = new Customer();
        customer.setId("tesId");
        customer.setName("Cahyo");
        customer.setPhone("1234567890");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        customer.setUserAccount(userAccount);
        when(customerRepository.findById("tesId")).thenReturn(Optional.of(customer));
        CustomerResponse result = customerService.deleteById("tesId");
        verify(customerRepository).delete(customer);
        verify(userAccountRepository).delete(userAccount);
        assertEquals(customer.getId(), result.getCustomerId());
        assertEquals(customer.getName(), result.getCustomerName());
        assertEquals(customer.getPhone(), result.getCustomerPhone());
        assertEquals(customer.getUserAccount().getUsername(), result.getCustomerUsername());
        assertEquals(customer.getUserAccount().getPassword(), result.getCustomerPassword());
        assertEquals(customer.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), result.getCustomerRole());
    }
}