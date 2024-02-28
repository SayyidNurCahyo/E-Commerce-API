package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.repository.TableRepository;
import com.enigma.wmbapi.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    @Override
    public Table addTable(Table table) {
        return tableRepository.saveAndFlush(table);
    }

    @Override
    public Table getTableById(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
    }

    @Override
    public Page<Table> getAllTable(SearchTableRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
//        Specification<Table> specification = TableSpecification.getSpecification(request);
        return tableRepository.findAll(page);
    }

    @Override
    public Table updateTable(Table table) {
        tableRepository.findById(table.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
        return tableRepository.saveAndFlush(table);
    }

    @Override
    public Table deleteById(String id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
        tableRepository.delete(table);
        return table;
    }
}
