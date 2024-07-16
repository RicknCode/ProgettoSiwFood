package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Category;
import com.uniroma3.prog.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uniroma3.prog.repository.RecipeRepository;

import java.util.List;

import static com.uniroma3.prog.controller.GlobalController.MAX_PAGE_OBJECTS;

@Service
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Transactional
	public Recipe findById(long id) {
		return recipeRepository.findById(id).get();
	}

	@Transactional
	public Recipe findByName(String name) { return recipeRepository.findByName(name).get(); }

	@Transactional
	public Iterable<Recipe> findAll(){
		return recipeRepository.findAll();
	}

	@Transactional
	public List<Recipe> getAllRecipes(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, MAX_PAGE_OBJECTS);
		return recipeRepository.findAll(pageable);
	}

	@Transactional
	public void saveRecipe(Recipe recipe) {
		this.recipeRepository.save(recipe);
	}

	@Transactional
	public void deleteRecipe(Long id) { this.recipeRepository.deleteById(id); }

	@Transactional(readOnly = true)
	public List<Recipe> searchRecipesByName(String keyword) {
		return recipeRepository.findByNameContainingIgnoreCase(keyword);
	}

	@Transactional(readOnly = true)
	public List<Recipe> searchRecipesByCategory(Category category) {
		return recipeRepository.findByCategoryNameContainingIgnoreCase(category.name());
	}

}
