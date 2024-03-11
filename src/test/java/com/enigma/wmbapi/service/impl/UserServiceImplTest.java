package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    private UserService userService;
    @BeforeEach
    void setUp(){
        userService = new UserServiceImpl(userAccountRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "username";
        UserAccount account = UserAccount.builder().username(username).build();
        when(userAccountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));
        UserAccount response = (UserAccount) userService.loadUserByUsername(username);
        assertEquals(response.getUsername(), username);
    }

    @Test
    void getByUserId() {
        String id = "id";
        UserAccount account = UserAccount.builder().id(id).build();
        when(userAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        UserAccount response = (UserAccount) userService.getByUserId(id);
        assertEquals(response.getId(), id);
    }

    @Test
    void getByContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("testUser", "testPassword"));
        UserAccount account = UserAccount.builder().id("userId").username("testUser").password("testPassword").build();
        when(userAccountRepository.findByUsername("testUser")).thenReturn(Optional.of(account));
        UserAccount userAccount = userService.getByContext();
        assertEquals("testUser", userAccount.getUsername());
    }
}