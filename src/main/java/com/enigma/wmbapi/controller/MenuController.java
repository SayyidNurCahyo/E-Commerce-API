package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.ImageResponse;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.MENU_API)
public class MenuController {
    private final MenuService menuService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<MenuResponse>> addMenu(@RequestPart(name = "menu") String jsonMenu,
                                                                @RequestPart(name = "image") List<MultipartFile> images) {
        CommonResponse<MenuResponse> response;
        try {
            NewMenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<NewMenuRequest>() {});
            request.setImages(images);
            MenuResponse menuAdded = menuService.addMenu(request);
            response = CommonResponse.<MenuResponse>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message(ResponseMessage.SUCCESS_SAVE_DATA)
                    .data(menuAdded).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (IOException e){
            response = CommonResponse.<MenuResponse>builder().message(ResponseMessage.ERROR_INTERNAL_SERVER)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<MenuResponse>> getMenuById(@PathVariable String id) {
        MenuResponse menu = menuService.getMenuById(id);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menu).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getAllMenu(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) Long price
    ) {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).price(price).build();
        Page<MenuResponse> menus = menuService.getAllMenu(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(menus.getTotalPages())
                .totalElement(menus.getTotalElements())
                .page(menus.getPageable().getPageNumber()+1)
                .size(menus.getPageable().getPageSize())
                .hasNext(menus.hasNext())
                .hasPrevious(menus.hasPrevious()).build();
        CommonResponse<List<MenuResponse>> response = CommonResponse.<List<MenuResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menus.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping
    public ResponseEntity<CommonResponse<MenuResponse>> updateMenu(@RequestPart(name = "menu") String jsonMenu,
                                                                   @RequestPart(name = "image", required = false) List<MultipartFile> images) {
        CommonResponse<MenuResponse> response;
        try {
            UpdateMenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<UpdateMenuRequest>() {});
            if (images!=null) request.setImages(images);
            MenuResponse menu = menuService.updateMenu(request);
            response = CommonResponse.<MenuResponse>builder()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                    .data(menu).build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }catch (IOException e){
            response = CommonResponse.<MenuResponse>builder().message(ResponseMessage.ERROR_INTERNAL_SERVER)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<MenuResponse>> deleteById(@PathVariable String id) {
        MenuResponse menu = menuService.deleteById(id);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data(menu).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
