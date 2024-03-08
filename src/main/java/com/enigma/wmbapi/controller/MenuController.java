package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.MENU_API)
public class MenuController {
    private final MenuService menuService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<MenuResponse>> addMenu(@RequestBody NewMenuRequest menu) {
        Menu menuAdded = menuService.addMenu(menu);
        MenuResponse menuResponse = MenuResponse.builder()
                .menuId(menuAdded.getId())
                .menuName(menuAdded.getName())
                .menuPrice(menuAdded.getPrice()).build();
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(menuResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<MenuResponse>> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);
        MenuResponse menuResponse = MenuResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice()).build();
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menuResponse).build();
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
        Page<Menu> menus = menuService.getAllMenu(request);
        Page<MenuResponse> menuResponses = menus.map(menu -> MenuResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice()).build());
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
                .data(menuResponses.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping
    public ResponseEntity<CommonResponse<MenuResponse>> updateMenu(@RequestBody UpdateMenuRequest request) {
        Menu menu = menuService.updateMenu(request);
        MenuResponse menuResponse = MenuResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice()).build();
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(menuResponse).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<MenuResponse>> deleteById(@PathVariable String id) {
        Menu menu = menuService.deleteById(id);
        MenuResponse menuResponse = MenuResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice()).build();
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data(menuResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
