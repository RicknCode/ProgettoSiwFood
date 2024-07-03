package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniroma3.prog.repository.RecipeRepository;

@Service
public class RecipeService {
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	public Recipe findById(long id) {
		return recipeRepository.findById(id).get();
	}
	public Iterable<Recipe> findAll(){
		return recipeRepository.findAll();
	}

}
