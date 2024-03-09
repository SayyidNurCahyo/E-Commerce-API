package com.enigma.wmbapi.service;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.TransTypeResponse;
import com.enigma.wmbapi.entity.TransType;

import java.util.List;

public interface TransTypeService {
    TransType getTransTypeById(TransTypeId id);
    List<TransTypeResponse> getAllTransType();
}
