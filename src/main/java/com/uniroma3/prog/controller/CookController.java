package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Cook;
import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.CookRepository;
import com.uniroma3.prog.repository.ImageRepository;
import com.uniroma3.prog.service.CookService;
import com.uniroma3.prog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CookController {

    @Autowired
    UserService userService;

    @Autowired
    CookService cookService;

    @Autowired
    CookRepository cookRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/cooks/{id}")
    public String getCook(@PathVariable("id")Long id, Model model) {
        model.addAttribute("cook", this.cookService.findById(id));
        model.addAttribute("cooks", this.cookService.findAll());
        return "cook";
    }

    @GetMapping("/cooks")
    public String showCooks(Model model) {
        model.addAttribute("cooks", this.cookService.findAll());
        return "cooks";
    }

    @GetMapping("/cooks/new")
    public String formNewCook(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = this.userService.getUser(authentication.getName());
            System.out.println(user.getName());
            model.addAttribute("user", user.getName());
            model.addAttribute("cook", new Cook());
            System.out.println("Done");
        }
        return "formNewCook";
    }

    @PostMapping(value={"/cook"}, consumes = "multipart/form-data")
    public String newRecipe(@Valid @ModelAttribute("cook") Cook cook, @RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                Image i=new Image();
                i.setImageData(file.getBytes());
                i.setMimeType(file.getContentType());

                cook.setImage(i);
                this.imageRepository.save(i);
            } catch (Exception e) {
                System.out.println("errore");
            }
            this.cookRepository.save(cook);
            model.addAttribute("cook", cook);
            return "cook";
        } else {
            return "formNewCook";
        }
    }

}
