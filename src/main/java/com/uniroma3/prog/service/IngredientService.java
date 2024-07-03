package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Ingredient;
import com.uniroma3.prog.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient findById(long id) {
        return ingredientRepository.findById(id).get();
    }

    public Iterable<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }

}
