package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {


}
