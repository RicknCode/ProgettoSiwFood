package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.*;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.ImageService;
import com.uniroma3.prog.service.RecipeService;
import com.uniroma3.prog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.uniroma3.prog.service.IngredientService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static com.uniroma3.prog.controller.GlobalController.MAX_PAGE_OBJECTS;

@Controller
@SessionAttributes("recipe")
public class RecipeController {

	@Autowired
	UserService userService;
	@Autowired
	CredentialsService credentialsService;
	@Autowired
	RecipeService recipeService;
	@Autowired
	ImageService imageService;
	@Autowired
	IngredientService ingredientService;

	@ModelAttribute("recipe")
	public Recipe createRecipeModel() {
		return new Recipe();
	}

	@GetMapping("/index")
	public String showIndex(Model model) {
		Map<String, List<Recipe>> categoriesRecipes = new TreeMap<>();
		for(Category category : Category.values()) {
			categoriesRecipes.put(category.name(), this.recipeService.searchRecipesByCategory(category));
		}
		model.addAttribute("categoriesRecipes", categoriesRecipes);
		return "index";
	}

	@GetMapping("/admin/index")
	public String showAdminIndex(Model model) {
		Map<String, List<Recipe>> categoriesRecipes = new TreeMap<>();
		for(Category category : Category.values()) {
			categoriesRecipes.put(category.name(), this.recipeService.searchRecipesByCategory(category));
		}
		model.addAttribute("categoriesRecipes", categoriesRecipes);
		return "index";
	}

	@GetMapping("/profile/my-recipes")
	public String getMyRecipes(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = this.credentialsService.getCredentials(userDetails.getUsername()).getUser();
		model.addAttribute("recipes", user.getRecipes());
		return "cook/profile-recipes";
	}

	@GetMapping("/recipe/modify/{id}")
	public String modifyMyRecipe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		return "cook/form-modify-recipe";
	}

	@PostMapping("/recipe/update")
	public String updateRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult recipeBindingResult, @RequestParam("file") MultipartFile file, BindingResult fileBindingResult, Model model) {
		if (recipeBindingResult.hasErrors() || fileBindingResult.hasErrors()) {
			return "redirect:/";
		}
		if (!file.isEmpty()) {
			try {
				Image i = new Image();
				i.setImageData(file.getBytes());
				i.setMimeType(file.getContentType());
				recipe.setImage(i);
				imageService.saveImage(i);
			}
			catch (Exception e) {
				e.printStackTrace();
				return "errorPage";
			}
		}
		for (Ingredient ingredient : recipe.getIngredients()) {
			this.ingredientService.saveIngredient(ingredient);
		}

		this.recipeService.saveRecipe(recipe);
		return "redirect:/";
	}

	@GetMapping(value = "/recipe/delete/{id}")
	public String deleteRecipe(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
		recipeService.deleteRecipe(id);
		String referer = request.getHeader("Referer");
		if (referer != null) {
			return "redirect:" + referer;
		}
		return "redirect:/";
	}

	@GetMapping("/recipe/{id}")
	public String getRecipe(@PathVariable("id")Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipe";
	}

	@GetMapping("/my-recipe/{id}")
	public String getMyRecipe(@PathVariable("id")Long id, Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = this.credentialsService.getCredentials(userDetails.getUsername()).getUser();
		Recipe recipe = this.recipeService.findById(id);
		for (Recipe r : user.getRecipes()) {
			if (r.getId() == id) {
				model.addAttribute("recipe", recipe);
				model.addAttribute("recipes", this.recipeService.findAll());
				return "cook/my-recipe";
			}
		}
		return "redirect:/";
	}

	@GetMapping("/recipes")
	public String showRecipes(Model model) {
		List<Recipe> recipes = (List<Recipe>) this.recipeService.findAll();
		model.addAttribute("recipes", recipes);
		return "recipes";
	}

	@GetMapping(value="/recipe/new")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		return "cook/formNewRecipe";
	}

	@PostMapping(value="/recipe/new/ingredients", consumes = "multipart/form-data")
	public String newRecipeFirstPart(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult recipeBindingResult, @RequestParam("numIngredients") int numIngredients, @RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		if (recipeBindingResult.hasErrors() || bindingResult.hasErrors()) {
			return "cook/formNewRecipe";
		}
		try {
			for (int i = 0; i < numIngredients; i++) {
				Ingredient ingredient = new Ingredient();
				recipe.getIngredients().add(ingredient);
			}
			Image i=new Image();
			i.setImageData(file.getBytes());
			i.setMimeType(file.getContentType());
			recipe.setImage(i);
			imageService.saveImage(i);
		} catch (Exception e) {
			e.printStackTrace();
			return "errorPage";
		}
		model.addAttribute("recipe", recipe);
		return "cook/formNewRecipeIngredients";
	}

	@PostMapping(value = "/recipe")
	public String newRecipeSecondPart(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult, SessionStatus sessionStatus, Model model) {
		if (bindingResult.hasErrors()) {
			return "cook/formNewRecipeIngredients";
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = this.credentialsService.getCredentials(userDetails.getUsername()).getUser();
		recipe.setUser(user);
		for (Ingredient ingredient : recipe.getIngredients()) {
			this.ingredientService.saveIngredient(ingredient);
		}
		this.recipeService.saveRecipe(recipe);
		user.getRecipes().add(recipe);
		userService.saveUser(user);
		sessionStatus.setComplete();
		return "cook/my-recipe";
	}

	@GetMapping(value = "/search-recipes")
	public String searchRecipe(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipes";
	}

	@GetMapping(value="/recipes/{categoryName}")
	public String showAppetizers(@PathVariable String categoryName, Model model) {
		Category category = Category.valueOf(categoryName.toUpperCase());
		List<Recipe> recipesList = recipeService.searchRecipesByCategory(category);
		model.addAttribute("recipes", recipesList);
		return "recipes";
	}

}
