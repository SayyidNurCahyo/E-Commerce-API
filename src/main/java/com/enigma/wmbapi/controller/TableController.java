package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.NewTableRequest;
import com.enigma.wmbapi.dto.request.SearchTableRequest;
import com.enigma.wmbapi.dto.request.UpdateTableRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TABLE_API)
public class TableController {
    private final TableService tableService;
    @PostMapping
    public ResponseEntity<CommonResponse<Table>> addTable(@RequestBody NewTableRequest table) {
        Table tableAdded = tableService.addTable(table);
        CommonResponse<Table> response = CommonResponse.<Table>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Table Added")
                .data(tableAdded).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<Table>> getTableById(@PathVariable String id) {
        Table table = tableService.getTableById(id);
        CommonResponse<Table> response = CommonResponse.<Table>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Table Exists")
                .data(table).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Table>>> getAllTable(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchTableRequest request = SearchTableRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).build();
        Page<Table> tables = tableService.getAllTable(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(tables.getTotalPages())
                .totalElement(tables.getTotalElements())
                .page(tables.getPageable().getPageNumber()+1)
                .size(tables.getPageable().getPageSize())
                .hasNext(tables.hasNext())
                .hasPrevious(tables.hasPrevious()).build();
        CommonResponse<List<Table>> response = CommonResponse.<List<Table>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Table Exists")
                .data(tables.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Table>> updateTable(@RequestBody UpdateTableRequest request) {
        Table update = tableService.updateTable(request);
        CommonResponse<Table> response = CommonResponse.<Table>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Data Table Updated")
                .data(update).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Table>> deleteById(@PathVariable String id) {
        Table table = tableService.deleteById(id);
        CommonResponse<Table> response = CommonResponse.<Table>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Table Deleted")
                .data(table).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
