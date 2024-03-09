package com.enigma.wmbapi.service;

import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image addImage(Menu menu, MultipartFile image);
    Resource getById(String id);
    void deleteById(String id);
}
