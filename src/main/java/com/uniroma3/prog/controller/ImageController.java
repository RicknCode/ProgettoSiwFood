package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    private String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/bmp":
                return "bmp";
            default:
                return "img";
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
        ByteArrayResource resource = new ByteArrayResource(image.getImageData());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getMimeType()))
                .body(resource);
    }

}
