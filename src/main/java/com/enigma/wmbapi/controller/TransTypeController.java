package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.dto.response.TransTypeResponse;
import com.enigma.wmbapi.entity.TransType;
import com.enigma.wmbapi.service.TransTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TransTypeResponse>>> getAllTransType() {
        List<TransTypeResponse> transTypes = transTypeService.getAllTransType();
        CommonResponse<List<TransTypeResponse>> response = CommonResponse.<List<TransTypeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transTypes).build();
        return ResponseEntity.ok(response);
    }
}
