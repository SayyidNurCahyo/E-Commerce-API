package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.dto.response.TableResponse;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.service.TableService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TABLE_API)
public class TableController {
    private final TableService tableService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<TableResponse>> addTable(@RequestBody NewTableRequest request) {
        TableResponse table = tableService.addTable(request);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(table).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<TableResponse>> getTableById(@PathVariable String id) {
        TableResponse table = tableService.getTableById(id);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(table).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TableResponse>>> getAllTable(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchTableRequest request = SearchTableRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).build();
        Page<TableResponse> tables = tableService.getAllTable(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(tables.getTotalPages())
                .totalElement(tables.getTotalElements())
                .page(tables.getPageable().getPageNumber()+1)
                .size(tables.getPageable().getPageSize())
                .hasNext(tables.hasNext())
                .hasPrevious(tables.hasPrevious()).build();
        CommonResponse<List<TableResponse>> response = CommonResponse.<List<TableResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(tables.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping
    public ResponseEntity<CommonResponse<TableResponse>> updateTable(@RequestBody UpdateTableRequest request) {
        TableResponse table = tableService.updateTable(request);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(table).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<TableResponse>> deleteById(@PathVariable String id) {
        TableResponse table = tableService.deleteById(id);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data(table).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
