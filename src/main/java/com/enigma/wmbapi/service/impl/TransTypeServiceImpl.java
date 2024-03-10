package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.TransTypeResponse;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.entity.TransType;
import com.enigma.wmbapi.repository.TransTypeRepository;
import com.enigma.wmbapi.service.TransTypeService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepository transTypeRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransType getTransTypeOrSave(TransTypeId id, String description) {
        return transTypeRepository.findById(id).orElseGet(()->transTypeRepository.saveAndFlush(TransType.builder().id(id).description(description).build()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransTypeResponse> getAllTransType() {
        List<TransType> transTypes = transTypeRepository.findAll();
        return transTypes.stream().map(transType -> TransTypeResponse.builder().transTypeId(transType.getId())
                .transTypeDescription(transType.getDescription()).build()).toList();
    }
}
