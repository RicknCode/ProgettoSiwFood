package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Ingredient;
import com.uniroma3.prog.service.IngredientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @GetMapping(value = "/ingredients")
    public String showIngredientsList(Model model) {
        List<Ingredient> ingredients = (List<Ingredient>) this.ingredientService.findAll();
        model.addAttribute("ingredients", ingredients);
        return "ingredients";
    }

    @GetMapping(value = "/ingredient/delete/{id}")
    public String deleteIngredient(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        ingredientService.deleteIngredient(id);
        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

}
