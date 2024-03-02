package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.entity.Menu;
import org.springframework.data.domain.Page;

public interface MenuService {
    Menu addMenu(NewMenuRequest menu);
    Menu getMenuById(String id);
    Page<Menu> getAllMenu(SearchMenuRequest request);
    Menu updateMenu(UpdateMenuRequest menu);
    Menu deleteById(String id);
}
