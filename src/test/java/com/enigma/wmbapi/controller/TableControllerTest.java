package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.dto.response.TableResponse;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.service.TableService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TableControllerTest {
    @MockBean
    private TableService tableService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addTable() throws Exception{
        NewTableRequest request = NewTableRequest.builder().name("name").build();
        TableResponse tableResponse = TableResponse.builder().tableId("id").tableName("name").build();
        when(tableService.addTable(any(NewTableRequest.class))).thenReturn(tableResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(APIUrl.TABLE_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<TableResponse>>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                    assertEquals(response.getData().getTableName(), request.getName());
                } );
    }

    @WithMockUser
    @Test
    void getTableById() throws Exception{
        String id = "id";
        TableResponse tableResponse = TableResponse.builder().tableId(id).tableName("name").build();
        when(tableService.getTableById(anyString())).thenReturn(tableResponse);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.TABLE_API+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<TableResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllTable() throws Exception{
        SearchTableRequest request = SearchTableRequest.builder().page(1).size(5).sortBy("name").direction("asc").build();
        List<TableResponse> tableResponses = List.of(TableResponse.builder().tableId("id").tableName("name").build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<TableResponse> tablePage = new PageImpl<>(tableResponses,page, tableResponses.size());
        when(tableService.getAllTable(any(SearchTableRequest.class))).thenReturn(tablePage);
        String stringRequest =objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.TABLE_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<TableResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateTable() throws Exception{
        UpdateTableRequest request = UpdateTableRequest.builder().id("id").name("name").build();
        TableResponse tableResponse = TableResponse.builder().tableId("id").tableName("name").build();
        when(tableService.updateTable(any(UpdateTableRequest.class))).thenReturn(tableResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put(APIUrl.TABLE_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<TableResponse>>() {});
                    assertEquals(202, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteById() throws Exception{
        String id = "id";
        TableResponse tableResponse = TableResponse.builder().tableId("id").tableName("name").build();
        when(tableService.deleteById(anyString())).thenReturn(tableResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(APIUrl.TABLE_API+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<TableResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, response.getMessage());
                } );
    }
}