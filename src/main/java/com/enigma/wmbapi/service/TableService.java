package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.entity.Table;
import org.springframework.data.domain.Page;

public interface TableService {
    Table addTable(NewTableRequest table);
    Table getTableById(String id);
    Page<Table> getAllTable(SearchTableRequest request);
    Table updateTable(UpdateTableRequest table);
    Table deleteById(String id);
}
