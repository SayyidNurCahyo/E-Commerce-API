package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.dto.request.AuthRequest;
import com.enigma.wmbapi.dto.request.RegisterRequest;
import com.enigma.wmbapi.dto.response.LoginResponse;
import com.enigma.wmbapi.dto.response.RegisterResponse;
import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.*;
import com.enigma.wmbapi.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerService customerService;
    @Mock
    private JwtService jwtService;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AdminService adminService;
    private AuthService authService;
    @BeforeEach
    void setUp(){
        authService = new AuthServiceImpl(userAccountRepository,roleService,passwordEncoder,customerService,jwtService,validationUtil,authenticationManager,adminService);
    }

    @Test
    void registerCustomer() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .password("testpassword")
                .name("Test User")
                .phone("1234567890")
                .build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("testpassword")
                .isEnabled(true)
                .roles(List.of(role))
                .build();
        when(roleService.getOrSave(UserRole.ROLE_CUSTOMER)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        doNothing().when(customerService).addCustomer(any(Customer.class));
        RegisterResponse response = authService.registerCustomer(request);
        assertEquals("testuser", response.getUsername());
        assertEquals(List.of("ROLE_CUSTOMER"), response.getRoles());
        verify(validationUtil).validate(request);
        verify(roleService).getOrSave(UserRole.ROLE_CUSTOMER);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
        verify(customerService).addCustomer(any(Customer.class));
    }

    @Test
    void registerAdmin() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .password("testpassword")
                .name("Test User")
                .phone("1234567890")
                .build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_ADMIN)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("testpassword")
                .isEnabled(true)
                .roles(List.of(role))
                .build();
        when(roleService.getOrSave(UserRole.ROLE_ADMIN)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        doNothing().when(adminService).addAdmin(any(Admin.class));
        RegisterResponse response = authService.registerAdmin(request);
        assertEquals("testuser", response.getUsername());
        assertEquals(List.of("ROLE_ADMIN"), response.getRoles());
        verify(validationUtil).validate(request);
        verify(roleService).getOrSave(UserRole.ROLE_ADMIN);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
        verify(adminService).addAdmin(any(Admin.class));
    }

    @Test
    void login() {
        AuthRequest request = new AuthRequest("username", "password");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("password").username("username")
                .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        String token = "token";
        LoginResponse expectedResponse = LoginResponse.builder()
                .username("username")
                .roles(List.of("ROLE_CUSTOMER"))
                .token(token)
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken(userAccount, null));
        when(jwtService.generateToken(userAccount)).thenReturn(token);
        LoginResponse actualResponse = authService.login(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userAccount);
    }
}