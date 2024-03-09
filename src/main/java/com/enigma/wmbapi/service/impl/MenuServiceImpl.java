package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.ImageResponse;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.MenuRepository;
import com.enigma.wmbapi.service.ImageService;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.util.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse addMenu(NewMenuRequest request) {
        validationUtil.validate(request);
        if (request.getImages().isEmpty()) throw new ConstraintViolationException("Menu Image is Required", null);
        Menu menu = Menu.builder().name(request.getName()).price(request.getPrice()).build();
        List<Image> images = new ArrayList<>();
        for (MultipartFile image:request.getImages()){
            Image imageAdded = imageService.addImage(menu, image);
            images.add(imageAdded);
        }
        menu.setImages(images);
//        menuRepository.saveAndFlush(menu);
        return convertToMenuResponse(menu);
    }

    @Transactional(readOnly = true)
    @Override
    public MenuResponse getMenuById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        return convertToMenuResponse(menu);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MenuResponse> getAllMenu(SearchMenuRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null || request.getPrice()!=null){
            Page<Menu> menus = menuRepository.findMenu(request.getName(), request.getPrice(), page);
            return convertToPageMenuResponse(menus);
        }else {
            return convertToPageMenuResponse(menuRepository.findAll(page));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse updateMenu(UpdateMenuRequest request) {
        validationUtil.validate(request);
        Menu menu = menuRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        if (request.getImages()!=null){
            List<Image> imageOlds = menu.getImages();
            for (Image imageOld:imageOlds){
                imageService.delete(imageOld);
            }
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile image: request.getImages()){
                Image imageNew = imageService.addImage(menu,image);
                imageList.add(imageNew);
            }
            menu.setImages(imageList);
        }
        menuRepository.saveAndFlush(menu);
        return convertToMenuResponse(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse deleteById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Menu Not Found"));
        for (Image image: menu.getImages()){
            imageService.delete(image);
        }
        menuRepository.delete(menu);
        return convertToMenuResponse(menu);
    }

    private MenuResponse convertToMenuResponse(Menu menu){
        return MenuResponse.builder().menuId(menu.getId()).menuName(menu.getName()).menuPrice(menu.getPrice())
                .imageResponses(menu.getImages().stream().map(image -> ImageResponse.builder()
                        .url(APIUrl.IMAGE_DOWNLOAD_API+"/"+image.getId())
                        .name(image.getName()).build()).toList()).build();
    }

    private Page<MenuResponse> convertToPageMenuResponse(Page<Menu> menuResponses){
        return menuResponses.map(this::convertToMenuResponse);
    }
}
