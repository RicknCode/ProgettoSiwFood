package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Image;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ImageRepository extends CrudRepository<Image, Long> {

    Optional<Image> findById(Long id);

}
