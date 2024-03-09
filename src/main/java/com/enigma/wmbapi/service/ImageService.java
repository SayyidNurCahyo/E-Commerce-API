package com.enigma.wmbapi.service;

import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image addImage(Menu menu, MultipartFile image);
    Resource getById(String id);
    Image getByName(String name);
    void delete(Image image);
}
