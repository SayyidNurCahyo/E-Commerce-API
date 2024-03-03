package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.TransType;
import com.enigma.wmbapi.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANS_TYPE_API)
public class TransTypeController {
    private final TransTypeService transTypeService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<TransType>> getTransTypeById(@PathVariable TransTypeId id) {
        TransType transType = transTypeService.getTransTypeById(id);
        CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transType).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransType>>> getAllTransType() {
        List<TransType> transTypes = transTypeService.getAllTransType();
        CommonResponse<List<TransType>> response = CommonResponse.<List<TransType>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transTypes).build();
        return ResponseEntity.ok(response);
    }
}
