package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.MenuRepository;
import com.enigma.wmbapi.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Override
    public Menu addMenu(Menu menu) {
        return menuRepository.saveAndFlush(menu);
    }

    @Override
    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
    }

    @Override
    public Page<Menu> getAllMenu(SearchMenuRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
//        Specification<Menu> specification = MenuSpecification.getSpecification(request);
        return menuRepository.findAll(page);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        menuRepository.findById(menu.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        return menuRepository.saveAndFlush(menu);
    }

    @Override
    public Menu deleteById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        menuRepository.delete(menu);
        return menu;
    }
}
