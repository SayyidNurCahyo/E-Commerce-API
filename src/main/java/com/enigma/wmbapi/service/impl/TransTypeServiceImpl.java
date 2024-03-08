package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.request.GetTransTypeRequest;
import com.enigma.wmbapi.entity.TransType;
import com.enigma.wmbapi.repository.TransTypeRepository;
import com.enigma.wmbapi.service.TransTypeService;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepository transTypeRepository;
    private final ValidationUtil validationUtil;

    @Transactional(readOnly = true)
    @Override
    public TransType getTransTypeById(TransTypeId id) {
        return transTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"TransType Not Found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransType> getAllTransType() {
        return transTypeRepository.findAll();
    }
}
