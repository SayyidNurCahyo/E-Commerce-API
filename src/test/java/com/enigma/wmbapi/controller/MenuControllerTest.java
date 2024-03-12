package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.NewMenuRequest;
import com.enigma.wmbapi.dto.response.CommonResponse;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.dto.response.ImageResponse;
import com.enigma.wmbapi.dto.response.MenuResponse;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {
    @MockBean
    private MenuService menuService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addMenu() throws Exception{
//        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
//        NewMenuRequest request = NewMenuRequest.builder().name("menu").price(10L).build();
//        MenuResponse menuResponse = MenuResponse.builder().menuId("id").menuName(request.getName()).menuPrice(request.getPrice()).imageResponses(List.of(ImageResponse.builder().name("timeNow_image.jpg").url(APIUrl.IMAGE_DOWNLOAD_API+"/imageId").build())).build();
//        when(menuService.addMenu(any(NewMenuRequest.class))).thenReturn(menuResponse);
//        String stringRequest = objectMapper.writeValueAsString(request);
//        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST,APIUrl.MENU_API)
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                        .)
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(result -> {
//                    CommonResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<MenuResponse>>() {});
//                    assertEquals(201, response.getStatusCode());
//                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
//                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getMenuById() {

    }

    @Test
    void getAllMenu() {
    }

    @Test
    void updateMenu() {
    }

    @Test
    void deleteById() {
    }
}