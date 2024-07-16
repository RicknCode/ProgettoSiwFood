package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.*;
import com.uniroma3.prog.service.ImageService;
import com.uniroma3.prog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CookController {

    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;

    @GetMapping("/cooks/{id}")
    public String getCook(@PathVariable("id")Long id, Model model) {
        User user = this.userService.findById(id);
        if (user != null) {
            model.addAttribute("cook", user);
            model.addAttribute("recipes", user.getRecipes());
            return "cook";
        } else {
            return "redirect:/cooks";
        }
    }

    @GetMapping("/cooks")
    public String showCooks(Model model) {
        model.addAttribute("cooks", this.userService.getAllUsers());
        return "cooks";
    }

    @GetMapping(value = "/cook/delete/{id}")
    public String deleteCook(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        userService.deleteUser(id);
        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

}
