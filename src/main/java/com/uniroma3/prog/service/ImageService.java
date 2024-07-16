package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveImage(Image image) {
        this.imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long id) { this.imageRepository.deleteById(id); }
}