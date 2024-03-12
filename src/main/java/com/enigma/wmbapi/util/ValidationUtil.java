package com.enigma.wmbapi.util;

import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.CustomerRepository;
import com.enigma.wmbapi.repository.MenuRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationUtil {
    private final Validator validator;
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;

    public void validate(Object o) {
        Set<ConstraintViolation<Object>> validate = validator.validate(o);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }
}
