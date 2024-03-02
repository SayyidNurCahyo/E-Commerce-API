package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.PagingResponse;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.MENU_API)
public class MenuController {
    private final MenuService menuService;
    @PostMapping
    public ResponseEntity<CommonResponse<Menu>> addMenu(@RequestBody NewMenuRequest menu) {
        Menu menuAdded = menuService.addMenu(menu);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Menu Added")
                .data(menuAdded).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<Menu>> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Menu Exists")
                .data(menu).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Menu>>> getAllMenu(
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
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(menus.getTotalPages())
                .totalElement(menus.getTotalElements())
                .page(menus.getPageable().getPageNumber()+1)
                .size(menus.getPageable().getPageSize())
                .hasNext(menus.hasNext())
                .hasPrevious(menus.hasPrevious()).build();
        CommonResponse<List<Menu>> response = CommonResponse.<List<Menu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Menu Exists")
                .data(menus.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Menu>> updateMenu(@RequestBody UpdateMenuRequest request) {
        Menu update = menuService.updateMenu(request);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Data Menu Updated")
                .data(update).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Menu>> deleteById(@PathVariable String id) {
        Menu menu = menuService.deleteById(id);
        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Menu Exists")
                .data(menu).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
