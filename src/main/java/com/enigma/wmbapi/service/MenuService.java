package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.MenuResponse;
import org.springframework.data.domain.Page;

public interface MenuService {
    MenuResponse addMenu(NewMenuRequest menu);
    MenuResponse getMenuById(String id);
    Page<MenuResponse> getAllMenu(SearchMenuRequest request);
    MenuResponse updateMenu(UpdateMenuRequest menu);
    MenuResponse deleteById(String id);
}
