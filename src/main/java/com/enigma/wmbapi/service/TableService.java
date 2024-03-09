package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.dto.response.TableResponse;
import org.springframework.data.domain.Page;

public interface TableService {
    TableResponse addTable(NewTableRequest table);
    TableResponse getTableById(String id);
    Page<TableResponse> getAllTable(SearchTableRequest request);
    TableResponse updateTable(UpdateTableRequest table);
    TableResponse deleteById(String id);
}
