package com.enigma.wmbapi.security;

import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final CustomerService customerService;
    private final UserService userService;
    public boolean hasId(String customerId){
        Customer customer = customerService.getCustomerById(customerId);
        UserAccount userAccount = userService.getByContext();
        if (!userAccount.getId().equals(customer.getUserAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ResponseMessage.ERROR_FORBIDDEN);
        }
        return true;
    }
}
