package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.dto.response.TransTypeResponse;
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
    public ResponseEntity<CommonResponse<TransTypeResponse>> getTransTypeById(@PathVariable TransTypeId id) {
        TransType transType = transTypeService.getTransTypeById(id);
        TransTypeResponse transTypeResponse = TransTypeResponse.builder().transTypeId(transType.getId())
                .transTypeDescription(transType.getDescription()).build();
        CommonResponse<TransTypeResponse> response = CommonResponse.<TransTypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transTypeResponse).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransTypeResponse>>> getAllTransType() {
        List<TransType> transTypes = transTypeService.getAllTransType();
        List<TransTypeResponse> transTypeResponses = transTypes.stream().map(transType -> TransTypeResponse.builder().transTypeId(transType.getId())
                .transTypeDescription(transType.getDescription()).build()).toList();
        CommonResponse<List<TransTypeResponse>> response = CommonResponse.<List<TransTypeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transTypeResponses).build();
        return ResponseEntity.ok(response);
    }
}
