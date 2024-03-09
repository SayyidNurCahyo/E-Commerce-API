package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.entity.Image;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.repository.ImageRepository;
import com.enigma.wmbapi.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final Path directoryPath;
    @Autowired
    public ImageServiceImpl(@Value("${wmbapi.multipart.path-location}") String directoryPath, ImageRepository imageRepository){
        this.directoryPath = Paths.get(directoryPath);
        this.imageRepository = imageRepository;
    }

    @PostConstruct
    public void initDirectory(){
        if (!Files.exists(directoryPath)){
            try {
                Files.createDirectory(directoryPath);
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
            }
        }
    }

    @Override
    public Image addImage(Menu menu, MultipartFile image) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(image.getContentType()))
                throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_CONTENT_TYPE,null);
            String imageName = System.currentTimeMillis()+"_"+image.getOriginalFilename();
            Path imagePath = directoryPath.resolve(imageName);
            Files.copy(image.getInputStream(),imagePath);
            Image imageSave = Image.builder().menu(menu).name(imageName).path(imagePath.toString())
                    .size(image.getSize()).contentType(image.getContentType()).build();
            return imageRepository.saveAndFlush(imageSave);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Resource getById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            return new UrlResource(filePath.toUri());
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            Files.delete(filePath);
            imageRepository.delete(image);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
