package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.repository.RoleRepository;
import com.enigma.wmbapi.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    private RoleService roleService;
    @BeforeEach
    void setUp(){
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void getOrSave() {
        UserRole userRole = UserRole.ROLE_CUSTOMER;
        Role role = Role.builder().id("roleId").role(UserRole.ROLE_CUSTOMER).build();
        when(roleRepository.findByRole(any(UserRole.class))).thenReturn(Optional.of(role));
        Role result = roleService.getOrSave(userRole);
        assertEquals(result.getRole(),role.getRole());
    }
}