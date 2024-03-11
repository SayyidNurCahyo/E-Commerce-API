package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.request.SearchMenuRequest;
import com.enigma.wmbapi.dto.request.UpdateMenuRequest;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.MenuRepository;
import com.enigma.wmbapi.service.ImageService;
import com.enigma.wmbapi.service.MenuService;
import com.enigma.wmbapi.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private ImageService imageService;
    private MenuService menuService;
    @BeforeEach
    void setUp(){
        menuService = new MenuServiceImpl(menuRepository, validationUtil, imageService);
    }

    @Test
    void addMenu() {
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        NewMenuRequest request = NewMenuRequest.builder()
                .name("Menu 1")
                .price(100L)
                .images(List.of(image))
                .build();
        Menu menu = new Menu();
        menu.setId("1");
        menu.setName("Menu 1");
        menu.setPrice(100L);
        String imageName = "timeNow_image.jpg";
        Path imagePath = Paths.get("/Users/Lenovo/OneDrive/Gambar/WMB_API/timeNow_image.jpg");
        Image imageSave = Image.builder().id("tesId").menu(menu).name(imageName).path(imagePath.toString())
                .size(image.getSize()).contentType(image.getContentType()).build();
        when(imageService.addImage(Mockito.any(Menu.class), Mockito.any(MultipartFile.class))).thenReturn(imageSave);
        menu.setImages(List.of(imageService.addImage(menu, image)));
        MenuResponse response = menuService.addMenu(request);
        assertEquals(request.getName(), response.getMenuName());
        assertEquals(request.getPrice(), response.getMenuPrice());
    }

    @Test
    void getMenuById() {
        String id = "1";
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName("Menu 1");
        menu.setPrice(100L);
        menu.setImages(List.of(Image.builder().id("tesId").menu(menu).name("timeNow_image.jpg").path(Paths.get("/Users/Lenovo/OneDrive/Gambar/WMB_API/timeNow_image.jpg").toString())
                .size(2L).contentType("image/jpeg").build()));
        Mockito.when(menuRepository.findById(id)).thenReturn(Optional.of(menu));
        MenuResponse actualResponse = menuService.getMenuById(id);
        assertEquals(menu.getId(), actualResponse.getMenuId());
    }

    @Test
    void getAllMenu() {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(1).size(10).price(10L).direction("asc").sortBy("name").name("Menu").build();
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Menu menu = Menu.builder().id("1").name("Menu")
                .price(10L).build();
        Image image = Image.builder().id("tesId").menu(menu).name("timeNow_image.jpg").path(Paths.get("/Users/Lenovo/OneDrive/Gambar/WMB_API/timeNow_image.jpg").toString())
                .size(2L).contentType("image/jpeg").build();
        menu.setImages(List.of(image));
        Page<Menu> menus = new PageImpl<>(List.of(menu));
        when(menuRepository.findMenu(request.getName(),request.getPrice(),page)).thenReturn(menus);
        Page<MenuResponse> result = menuService.getAllMenu(request);
        assertEquals(result.getNumber(), menus.getNumber());
        assertEquals(result.getSize(), menus.getSize());
    }

    @Test
    void updateMenu() {
        UpdateMenuRequest request = UpdateMenuRequest.builder()
                .id("1").name("Menu Update")
                .price(200L)
                .build();
        Menu menu = new Menu();
        menu.setId("1");
        menu.setName("Menu 1");
        menu.setPrice(100L);
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_"+image.getName();
        Path imagePath = Paths.get("/Users/Lenovo/OneDrive/Gambar/WMB_API/"+imageName);
        Image imageMenu = Image.builder().id("tesId").menu(menu).name(imageName).path(imagePath.toString())
                .size(image.getSize()).contentType(image.getContentType()).build();
        menu.setImages(List.of(imageMenu));
        when(menuRepository.findById(request.getId())).thenReturn(Optional.of(menu));
        Menu menuUpdate = Menu.builder().id(request.getId()).name(request.getName()).price(request.getPrice()).images(menu.getImages()).build();
        MenuResponse response = menuService.updateMenu(request);
        assertEquals(response.getMenuName(),menuUpdate.getName());
        assertEquals(response.getMenuPrice(),menuUpdate.getPrice());
    }

    @Test
    void deleteById() {
        String id = "1";
        Menu menu = new Menu();
        menu.setId("1");
        menu.setName("Menu 1");
        menu.setPrice(100L);
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_"+image.getName();
        Path imagePath = Paths.get("/Users/Lenovo/OneDrive/Gambar/WMB_API/"+imageName);
        Image imageMenu = Image.builder().id("tesId").menu(menu).name(imageName).path(imagePath.toString())
                .size(image.getSize()).contentType(image.getContentType()).build();
        menu.setImages(List.of(imageMenu));
        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));
        MenuResponse response = menuService.deleteById(id);
        assertEquals(response.getMenuId(),menu.getId());
        assertEquals(response.getMenuName(),menu.getName());
    }
}