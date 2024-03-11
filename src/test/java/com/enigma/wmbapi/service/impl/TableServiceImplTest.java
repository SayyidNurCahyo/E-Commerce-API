package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.dto.response.TableResponse;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.repository.TableRepository;
import com.enigma.wmbapi.service.TableService;
import com.enigma.wmbapi.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TableServiceImplTest {
    @Mock
    private TableRepository tableRepository;
    @Mock
    private ValidationUtil validationUtil;
    private TableService tableService;
    @BeforeEach
    void setUp(){
        tableService = new TableServiceImpl(tableRepository,validationUtil);
    }

    @Test
    void addTable() {
        NewTableRequest request = NewTableRequest.builder().name("tableName").build();
        Table table = Table.builder().name(request.getName()).build();
        when(tableRepository.saveAndFlush(any(Table.class))).thenReturn(table);
        TableResponse response = tableService.addTable(request);
        assertEquals(response.getTableName(),request.getName());
    }

    @Test
    void getTableById() {
        String id = "tableId";
        Table table = Table.builder().id(id).name("tableName").build();
        when(tableRepository.findById(anyString())).thenReturn(Optional.of(table));
        TableResponse response = tableService.getTableById(id);
        assertEquals(response.getTableId(), id);
    }

    @Test
    void getAllTable() {
        SearchTableRequest request = SearchTableRequest.builder().direction("asc").name("tableName").page(1).size(5).sortBy("name").build();
        Page<Table> tablePage = new PageImpl<>(List.of(Table.builder().id("tableId").name(request.getName()).build()));
        when(tableRepository.findTable(anyString(),any(Pageable.class))).thenReturn(tablePage);
        Page<TableResponse> responses = tableService.getAllTable(request);
        assertEquals(responses.getContent().get(0).getTableName(), request.getName());
    }

    @Test
    void updateTable() {
        UpdateTableRequest request = UpdateTableRequest.builder().id("tableId").name("tableName").build();
        Table table = Table.builder().id(request.getId()).name(request.getName()).build();
        when(tableRepository.findById(anyString())).thenReturn(Optional.of(table));
        when(tableRepository.saveAndFlush(any(Table.class))).thenReturn(table);
        TableResponse response = tableService.updateTable(request);
        assertEquals(response.getTableName(),request.getName());
    }

    @Test
    void deleteById() {
        String id = "tableId";
        Table table = Table.builder().id(id).name("tableName").build();
        when(tableRepository.findById(anyString())).thenReturn(Optional.of(table));
        doNothing().when(tableRepository).delete(table);
        TableResponse response = tableService.deleteById(id);
        verify(tableRepository,times(1)).delete(table);
    }
}