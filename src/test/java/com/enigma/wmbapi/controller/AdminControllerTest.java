package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @MockBean
    private AdminService adminService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "Cahyo", roles = "ADMIN")
    @Test
    void getAdminById() throws Exception {
        String id = UUID.randomUUID().toString();
        AdminResponse adminResponse = AdminResponse.builder().adminId(id).adminName("Cahyo").adminPassword("password").adminRole(List.of("ADMIN")).adminUsername("Cahyo").adminPhone("088888").build();
        when(adminService.getAdminById(anyString())).thenReturn(adminResponse);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.ADMIN_API+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
            CommonResponse<AdminResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<AdminResponse>>() {});
            assertEquals(200, response.getStatusCode());
            assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
        } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllAdmin() throws Exception{
        SearchAdminRequest request = SearchAdminRequest.builder().page(1).size(5).sortBy("name").direction("asc").build();
        List<AdminResponse> adminResponses = List.of(AdminResponse.builder().adminId("id").adminName("Cahyo").adminPassword("password").adminRole(List.of("ADMIN")).adminUsername("Cahyo").adminPhone("088888").build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<AdminResponse> adminResponsePage = new PageImpl<>(adminResponses,page, adminResponses.size());
        when(adminService.getAllAdmin(any(SearchAdminRequest.class))).thenReturn(adminResponsePage);
        String stringRequest =objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.ADMIN_API).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<AdminResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<List<AdminResponse>>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateAdmin() throws Exception {
        UpdateAdminRequest request = UpdateAdminRequest.builder().id("id").name("name").phone("08888").username("user").build();
        AdminResponse adminResponse = AdminResponse.builder().adminPhone(request.getPhone()).adminUsername(request.getUsername()).adminId(request.getId()).adminName(request.getName()).build();
        when(adminService.updateAdmin(any(UpdateAdminRequest.class))).thenReturn(adminResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put(APIUrl.ADMIN_API).contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<AdminResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<AdminResponse>>() {});
                    assertEquals(202, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteById() throws Exception{
        String id = "id";
        AdminResponse adminResponse = AdminResponse.builder().adminId(id).build();
        when(adminService.updateAdmin(any(UpdateAdminRequest.class))).thenReturn(adminResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(APIUrl.ADMIN_API+"/"+id).contentType(MediaType.APPLICATION_JSON_VALUE).content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<AdminResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<AdminResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, response.getMessage());
                } );
    }
}