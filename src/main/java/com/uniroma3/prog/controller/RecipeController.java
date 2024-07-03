package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Recipe;
import com.uniroma3.prog.service.RecipeService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.repository.ImageRepository;
import com.uniroma3.prog.repository.RecipeRepository;

import jakarta.validation.Valid;

@Controller
public class RecipeController {

	@Autowired
	RecipeService recipeService;
	@Autowired
	RecipeRepository recipeRepository;
	@Autowired
	ImageRepository imageRepository;

	@GetMapping("/")
	public String showIndex(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "index";
	}

	@GetMapping("/recipes/{id}")
	public String getRecipe(@PathVariable("id")Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipe";
	}
	
	@GetMapping("/recipes")
	public String showRecipes(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipes";
	}
	
	@GetMapping(value="/recipes/new")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		return "formNewRecipe";
	}
	
	@PostMapping(value={"/recipe"}, consumes = "multipart/form-data")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, @RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				Image i=new Image();
				i.setImageData(file.getBytes());
				i.setMimeType(file.getContentType());

				recipe.setImage(i);
				this.imageRepository.save(i);
			} catch (Exception e) {
				System.out.println("errore");
			}
			this.recipeRepository.save(recipe);
			model.addAttribute("recipe", recipe);
			return "recipe";
		} else {
			return "formNewRecipe";
		}
	}

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
				.contentType(MediaType.parseMediaType(image.getMimeType()))  // or the appropriate content type
				.body(resource);
	}

}
