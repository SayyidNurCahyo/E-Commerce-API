package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.security.AuthenticatedUser;
import com.enigma.wmbapi.service.AdminService;
import com.enigma.wmbapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.ADMIN_API)
public class AdminController {
    private final AdminService adminService;
    private final AuthenticatedUser authenticatedUser;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<AdminResponse>> getAdminById(@PathVariable String id) {
        AdminResponse admin = adminService.getAdminById(id);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(admin).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<AdminResponse>>> getAllAdmin(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone
    ) {
        SearchAdminRequest request = SearchAdminRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).phone(phone).build();
        Page<AdminResponse> admins = adminService.getAllAdmin(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(admins.getTotalPages())
                .totalElement(admins.getTotalElements())
                .page(admins.getPageable().getPageNumber()+1)
                .size(admins.getPageable().getPageSize())
                .hasNext(admins.hasNext())
                .hasPrevious(admins.hasPrevious()).build();
        CommonResponse<List<AdminResponse>> response = CommonResponse.<List<AdminResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(admins.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<AdminResponse>> updateAdmin(@RequestBody UpdateAdminRequest request) {
        AdminResponse admin = adminService.updateAdmin(request);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(admin).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<AdminResponse>> deleteById(@PathVariable String id) {
        AdminResponse admin = adminService.deleteById(id);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data(admin).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
