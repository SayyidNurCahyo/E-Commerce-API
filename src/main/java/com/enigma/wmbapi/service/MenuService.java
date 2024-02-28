package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Menu;
import org.springframework.data.domain.Page;

public interface MenuService {
    Menu addMenu(Menu menu);
    Menu getMenuById(String id);
    Page<Menu> getAllMenu(SearchMenuRequest request);
    Menu updateMenu(Menu menu);
    Menu deleteById(String id);
}
