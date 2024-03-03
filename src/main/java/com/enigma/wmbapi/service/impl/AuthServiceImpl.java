package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.dto.request.AuthRequest;
import com.enigma.wmbapi.dto.request.RegisterRequest;
import com.enigma.wmbapi.dto.response.LoginResponse;
import com.enigma.wmbapi.dto.response.RegisterResponse;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.AuthService;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.service.JwtService;
import com.enigma.wmbapi.service.RoleService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest request) throws DataIntegrityViolationException {
        validationUtil.validate(request);
        Role role=roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(role))
                .isEnabled(true).build();
        userAccountRepository.saveAndFlush(account);
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .userAccount(account).build();
        customerService.addCustomer(customer);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roleAuth).build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        validationUtil.validate(request);
        Role role=roleService.getOrSave(UserRole.ROLE_ADMIN);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(role))
                .isEnabled(true).build();
        userAccountRepository.saveAndFlush(account);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roleAuth).build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginResponse login(AuthRequest request) {
        UserAccount account = userAccountRepository.findUserAccount(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Account Not Found, Register First"));
        if(!passwordEncoder.matches(request.getPassword(), account.getPassword())) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Account Not Found, Register First");
        String token = jwtService.generateToken();
        return LoginResponse.builder()
                .username(request.getUsername())
                .token(token)
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
