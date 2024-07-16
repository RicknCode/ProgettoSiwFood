package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Recipe;
import com.uniroma3.prog.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    //@Autowired
    //CookService cookService;

    @Autowired
    RecipeService recipeService;

    @PostMapping(value="/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Recipe> searchResults = recipeService.searchRecipesByName(keyword);
        model.addAttribute("recipes", searchResults);
        return "recipes";
    }

}
