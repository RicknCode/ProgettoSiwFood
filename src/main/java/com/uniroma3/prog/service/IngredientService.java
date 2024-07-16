package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Ingredient;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional
    public Ingredient findById(long id) {
        return ingredientRepository.findById(id).get();
    }
    @Transactional
    public Iterable<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }
    @Transactional
    public Ingredient saveIngredient(Ingredient ingredient) {
        return this.ingredientRepository.save(ingredient);
    }
    @Transactional
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
