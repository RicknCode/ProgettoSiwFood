package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.RecipeRepository;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    //@GetMapping(value = "/")
    //public String index(Model model) {
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (authentication instanceof AnonymousAuthenticationToken) {
    //        return "index";
    //    }
    //    else {
    //        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    //        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
    //            return "admin/indexAdmin";
    //        }
    //    }
    //    return "index";
    //}

    @GetMapping(value = "/login")
    public String showLoginForm(Model model) {
        return "loginForm";
    }

    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "index";
    }

    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "registerForm";
    }

    @PostMapping(value = "/register")
    public String registerUser(
             @Valid @ModelAttribute("user") User user,
             BindingResult userBindingResult,
             @Valid @ModelAttribute("credentials") Credentials credentials,
             BindingResult credentialsBindingResult,
             Model model)
    {
        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            user.setUsername(credentials.getUsername());
            userService.saveUser(user);
            credentials.setUser(user);
            credentials.setRole("DEFAULT");
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "registerForm";
    }

    @GetMapping(value = "/profile/upgrade-cook")
    private String upgradeToCook(Model model) {
        System.out.println("Setting  Role to COok");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Credentials credentials = this.credentialsService.getCredentials(authentication.getName());
            credentials.setRole("COOK");
            credentialsService.saveCredentials(credentials);
            System.out.println("Done");
        }
        return "index";
    }

    @GetMapping(value = "/about")
    public String showAboutUs(Model model) {
        return "aboutUs";
    }
}
