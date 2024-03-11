package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.ImageRepository;
import com.enigma.wmbapi.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private Path directoryPath;
    private ImageService imageService;
    @BeforeEach
    void setUp(){
        imageService = new ImageServiceImpl("/Users/Lenovo/OneDrive/Gambar/WMB_API", imageRepository);
    }

    @Test
    void addImage() {
        Menu menu = new Menu();
        menu.setId("1");
        menu.setName("Menu 1");
        menu.setPrice(100L);
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_image.jpg";
        when(directoryPath.resolve(Mockito.anyString())).thenReturn(Path.of("/Users/Lenovo/OneDrive/Gambar/WMB_API/timeNow_image.jpg"));
        Path imagePath = directoryPath.resolve(imageName);
        Image imageSave = Image.builder().menu(menu).name(imageName).path(imagePath.toString())
                .size(image.getSize()).contentType(image.getContentType()).build();
        when(imageRepository.saveAndFlush(Mockito.any(Image.class))).thenReturn(imageSave);
        imageService.addImage(menu, image);
        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
    }

    @Test
    void getByName() {
        String name = "image_name";
        Image expectedImage = new Image();
        when(imageRepository.findByName(name)).thenReturn(Optional.of(expectedImage));
        Image actualImage = imageService.getByName(name);
        assertEquals(expectedImage, actualImage);
    }

    @Test
    void getById() {
        String id = "id";
        Image image = Image.builder()
                .id(id)
                .path("/Users/Lenovo/OneDrive/Gambar/WMB_API/1709990392567_bakwan3.jpg")
                .build();
        Resource expectedResource = null;
        try {
            expectedResource = new UrlResource(Paths.get(image.getPath()).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        Resource actualResource = imageService.getById(id);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    void delete() {
        String name = "image_name";
        Image expectedImage = new Image();
        when(imageRepository.findByName(name)).thenReturn(Optional.of(expectedImage));
        Image actualImage = imageService.getByName(name);
        assertEquals(expectedImage, actualImage);
    }
}