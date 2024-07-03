package com.uniroma3.prog.repository;

import com.uniroma3.prog.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
