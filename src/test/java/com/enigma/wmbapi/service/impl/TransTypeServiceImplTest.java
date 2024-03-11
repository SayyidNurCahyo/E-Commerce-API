package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.TransTypeResponse;
import com.enigma.wmbapi.entity.TransType;
import com.enigma.wmbapi.repository.TransTypeRepository;
import com.enigma.wmbapi.service.TransTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransTypeServiceImplTest {
    @Mock
    private TransTypeRepository transTypeRepository;
    private TransTypeService transTypeService;
    @BeforeEach
    void setUp(){
        transTypeService = new TransTypeServiceImpl(transTypeRepository);
    }

    @Test
    void getTransTypeOrSave() {
        TransTypeId id = TransTypeId.EI;
        String desc = "Eat In";
        TransType transType = TransType.builder().id(id).description(desc).build();
        when(transTypeRepository.findById(any(TransTypeId.class))).thenReturn(Optional.of(transType));
        TransType response = transTypeService.getTransTypeOrSave(id,desc);
        assertEquals(response.getDescription(),desc);
    }

    @Test
    void getAllTransType() {
        List<TransType> transTypes = List.of(TransType.builder().id(TransTypeId.EI).description("desc").build());
        when(transTypeRepository.findAll()).thenReturn(transTypes);
        List<TransTypeResponse> responses = transTypeService.getAllTransType();
        assertEquals(responses.size(), transTypes.size());
    }
}