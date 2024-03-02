package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.repository.TableRepository;
import com.enigma.wmbapi.service.TableService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Table addTable(NewTableRequest request) {
        validationUtil.validate(request);
        Table table = Table.builder().name(request.getName()).build();
        return tableRepository.saveAndFlush(table);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Table getTableById(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<Table> getAllTable(SearchTableRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if (request.getName()!=null){
            return tableRepository.findTable(request.getName(),page);
        }else {
            return tableRepository.findAll(page);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Table updateTable(UpdateTableRequest request) {
        validationUtil.validate(request);
        tableRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
        Table table = Table.builder().id(request.getId()).name(request.getName()).build();
        return tableRepository.saveAndFlush(table);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Table deleteById(String id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table Not Found"));
        tableRepository.delete(table);
        return table;
    }
}
