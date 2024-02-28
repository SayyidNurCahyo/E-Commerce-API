package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.SearchTransTypeRequest;
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
    @PostMapping
    public ResponseEntity<CommonResponse<TransType>> addTransType(@RequestBody TransType transType) {
        TransType transTypeAdded = transTypeService.addTransType(transType);
        CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data TransType Added")
                .data(transTypeAdded).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<TransType>> getTransTypeById(@PathVariable String id) {
        TransType transType = transTypeService.getTransTypeById(id);
        CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transType).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransType>>> getAllTransType(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchTransTypeRequest request = SearchTransTypeRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).build();
        Page<TransType> transTypes = transTypeService.getAllTransType(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transTypes.getTotalPages())
                .totalElement(transTypes.getTotalElements())
                .page(transTypes.getPageable().getPageNumber()+1)
                .size(transTypes.getPageable().getPageSize())
                .hasNext(transTypes.hasNext())
                .hasPrevious(transTypes.hasPrevious()).build();
        CommonResponse<List<TransType>> response = CommonResponse.<List<TransType>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transTypes.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<TransType>> updateTransType(@RequestBody TransType product) {
        TransType update = transTypeService.updateTransType(product);
        CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Data TransType Updated")
                .data(update).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<TransType>> deleteById(@PathVariable String id) {
        TransType transType = transTypeService.deleteById(id);
        CommonResponse<TransType> response = CommonResponse.<TransType>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data TransType Exists")
                .data(transType).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
