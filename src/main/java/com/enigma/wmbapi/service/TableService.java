package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.entity.Table;
import org.springframework.data.domain.Page;

public interface TableService {
    Table addTable(Table table);
    Table getTableById(String id);
    Page<Table> getAllTable(SearchTableRequest request);
    Table updateTable(Table table);
    Table deleteById(String id);
}
