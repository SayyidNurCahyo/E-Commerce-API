package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.GetTransTypeRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.entity.TransType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransTypeService {
    TransType getTransTypeById(String id);
    List<TransType> getAllTransType();
    TransType getOrSave(GetTransTypeRequest transType);
}
