package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.MenuRepository;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Menu addMenu(NewMenuRequest request) {
        validationUtil.validate(request);
        Menu menu = Menu.builder().name(request.getName()).price(request.getPrice()).build();
        return menuRepository.saveAndFlush(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<Menu> getAllMenu(SearchMenuRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null || request.getPrice()!=null){
            return menuRepository.findMenu(request.getName(), request.getPrice(), page);
        }else {
            return menuRepository.findAll(page);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Menu updateMenu(UpdateMenuRequest request) {
        validationUtil.validate(request);
        menuRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        Menu menu = Menu.builder().id(request.getId()).name(request.getName()).price(request.getPrice()).build();
        return menuRepository.saveAndFlush(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Menu deleteById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        menuRepository.delete(menu);
        return menu;
    }
}
