package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.repository.AdminRepository;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.AdminService;
import com.enigma.wmbapi.util.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private UserAccountRepository userAccountRepository;
    private AdminService adminService;
    @BeforeEach
    void setUp(){
        adminService = new AdminServiceImpl(adminRepository, validationUtil, userAccountRepository);
    }


    @Test
    void addAdmin() {
        Admin admin = Admin.builder().id("tes id").name("cahyo").phone("088888")
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        when(adminRepository.saveAndFlush(Mockito.any(Admin.class))).thenReturn(admin);
        Admin result = adminRepository.saveAndFlush(admin);
        assertEquals(result.getUserAccount().getUsername(), admin.getUserAccount().getUsername());
    }

    @Test
    void getAdminById() {
        String id = "tes id";
        Admin admin = Admin.builder().id("tes id").name("cahyo").phone("088888")
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admin));
        AdminResponse result = adminService.getAdminById(id);
        assertEquals(result.getAdminUsername(), admin.getUserAccount().getUsername());
    }

    @Test
    void getAllAdmin() {
        SearchAdminRequest request = SearchAdminRequest.builder().size(5)
                .page(1).direction("asc").sortBy("name").build();
        Page<Admin> adminPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(adminRepository.findAll(Mockito.any(Pageable.class))).thenReturn(adminPage);
        Page<AdminResponse> result = adminService.getAllAdmin(request);
        assertEquals(adminPage.getTotalElements(), result.getTotalElements());
        assertEquals(adminPage.getTotalPages(), result.getTotalPages());
        assertEquals(adminPage.getNumber(), result.getNumber());
        assertEquals(adminPage.getSize(), result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void updateAdmin() {
        UpdateAdminRequest request = new UpdateAdminRequest();
        request.setId("tesId");
        request.setName("Cahyo");
        request.setPhone("1234567890");
        request.setUsername("cahyo123");
        Admin admin = new Admin();
        admin.setId("tesId");
        admin.setName("Cahyo2");
        admin.setPhone("0987654321");
        UserAccount account = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build();
        account.setUsername("Cahyo234");
        admin.setUserAccount(account);
        when(adminRepository.findById(request.getId())).thenReturn(Optional.of(admin));
        AdminResponse response = adminService.updateAdmin(request);
        assertEquals(request.getName(), response.getAdminName());
        assertEquals(request.getPhone(), response.getAdminPhone());
        assertEquals(request.getUsername(), response.getAdminUsername());
    }

    @Test
    void deleteById() {
        Admin admin = new Admin();
        admin.setId("tesId");
        admin.setName("Cahyo");
        admin.setPhone("1234567890");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnabled(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build();
        admin.setUserAccount(userAccount);
        when(adminRepository.findById("tesId")).thenReturn(Optional.of(admin));
        AdminResponse result = adminService.deleteById("tesId");
        verify(adminRepository).delete(admin);
        verify(userAccountRepository).delete(userAccount);
        assertEquals(admin.getId(), result.getAdminId());
        assertEquals(admin.getName(), result.getAdminName());
        assertEquals(admin.getPhone(), result.getAdminPhone());
        assertEquals(admin.getUserAccount().getUsername(), result.getAdminUsername());
        assertEquals(admin.getUserAccount().getPassword(), result.getAdminPassword());
        assertEquals(admin.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), result.getAdminRole());
    }
}