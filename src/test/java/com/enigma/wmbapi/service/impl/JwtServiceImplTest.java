package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.dto.response.JwtClaims;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {
    private JwtService jwtService;
    @BeforeEach
    void setUp(){
        jwtService = new JwtServiceImpl("d2FydW5nIG1ha2FuIGJhaGFyaSBhcGk=","WMB API", 86400L);
    }

    @Test
    void generateToken() {
        JwtServiceImpl jwtService = new JwtServiceImpl("d2FydW5nIG1ha2FuIGJhaGFyaSBhcGk=", "WMB API", 86400L);
        UserAccount account = UserAccount.builder()
                .id("123").username("testUser").isEnabled(true).password("testPassword")
                .roles(List.of(Role.builder()
                        .id("roleId").role(UserRole.ROLE_CUSTOMER).build())).build();
        String token = jwtService.generateToken(account);
        assertNotNull(token);
    }

    @Test
    void verifyJwtToken() {
        JwtServiceImpl jwtService = new JwtServiceImpl("d2FydW5nIG1ha2FuIGJhaGFyaSBhcGk=", "WMB API", 86400L);
        String token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwY2I5Y2M1NS0xZmRhLTRhNDgtYmRlNy1hYTljMzMwMjNiYmUiLCJyb2xlcyI6WyJST0xFX0NVU1RPTUVSIl0sImlhdCI6MTcxMDAwMjc1NywiZXhwIjoxNzEwMDg5MTU3LCJpc3MiOiJXTUIgQVBJIn0.VqTGmcJnBJs1SWsvnQdEoUDqpx5xf7ju1Pnp3M1EBTEXEwLQF29w_3Brib_tu_IH2Dq-s_P3QT219ma0APG5mw";
        Boolean result = jwtService.verifyJwtToken(token);
        assertTrue(result);
    }

    @Test
    void getClaimsByToken() {
        JwtServiceImpl jwtService = new JwtServiceImpl("d2FydW5nIG1ha2FuIGJhaGFyaSBhcGk=", "WMB API", 86400L);
        UserAccount account = UserAccount.builder()
                .id("0cb9cc55-1fda-4a48-bde7-aa9c33023bbe").username("testUser").isEnabled(true).password("testPassword")
                .roles(List.of(Role.builder()
                        .id("roleId").role(UserRole.ROLE_CUSTOMER).build())).build();
        String token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwY2I5Y2M1NS0xZmRhLTRhNDgtYmRlNy1hYTljMzMwMjNiYmUiLCJyb2xlcyI6WyJST0xFX0NVU1RPTUVSIl0sImlhdCI6MTcxMDAwMjc1NywiZXhwIjoxNzEwMDg5MTU3LCJpc3MiOiJXTUIgQVBJIn0.VqTGmcJnBJs1SWsvnQdEoUDqpx5xf7ju1Pnp3M1EBTEXEwLQF29w_3Brib_tu_IH2Dq-s_P3QT219ma0APG5mw";
        JwtClaims result = jwtService.getClaimsByToken(token);
        assertNotNull(result);
        assertEquals("0cb9cc55-1fda-4a48-bde7-aa9c33023bbe", result.getUserAccountId());
        assertEquals(Collections.singletonList("ROLE_CUSTOMER"), result.getRoles());
    }
}