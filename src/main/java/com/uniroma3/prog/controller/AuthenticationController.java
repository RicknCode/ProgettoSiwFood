package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.*;
import com.uniroma3.prog.repository.RoleRepository;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.ImageService;
import com.uniroma3.prog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String showIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/index";
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            for (Role role : credentials.getRoles()) {
                if (role.getName().equals(Credentials.ADMIN_ROLE))
                    return "redirect:/admin/index";
            }
        }
        return "redirect:/index";
    }

    @GetMapping(value = "/profile/index")
    public String showProfile(Model model) {
        return "cook/profile-index";
    }

    @GetMapping(value = "/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            model.addAttribute("referer", referer);
        }
        return "loginForm";
    }

    @GetMapping(value = "/success")
    public String defaultAfterLogin(@RequestParam(required = false) String referer, Model model) {
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registerForm";
    }

    @GetMapping(value = "/admin/add-cook")
    public String showAddCookForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "admin/add-cook";
    }

    @GetMapping(value = "/cook/modify/{id}")
    public String modifyCook(@PathVariable("id") Long id, Model model) {
        User user = this.userService.findById(id);
        Credentials credentials = user.getCredentials();
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setUser(user);
        registrationForm.setCredentials(credentials);
        model.addAttribute("registrationForm", registrationForm);
        return "admin/modify-cook";
    }

    @PostMapping(value = "/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
            BindingResult userBindingResult, HttpServletRequest request,
            Model model)
    {
        User user = registrationForm.getUser();
        Credentials credentials = registrationForm.getCredentials();
        MultipartFile file = registrationForm.getFile();

        if (credentialsService.existsByUsername(credentials.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "registerForm";
        }
        if (userBindingResult.hasErrors()) {
            model.addAttribute("error", "Binding errors occurred");
            return "registerForm";
        }
        try {
            user.setCredentials(credentials);
            credentials.setUser(user);
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByName("ROLE_COOK").get());
            credentials.updateRoles(roles);

            Image i = new Image();
            i.setImageData(file.getBytes());
            i.setMimeType(file.getContentType());
            user.setImage(i);
            imageService.saveImage(i);

            userService.saveUser(user);
            credentialsService.saveCredentials(credentials);
            if (request.getHeader("Referer").contains("/admin/add-cook")) {
                return "admin/add-cook-successful";
            }
            return "registration-successful";
        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "registerForm";
        }
    }

    @PostMapping(value = "/cook/modify")
    public String updateCook(
            @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
            BindingResult bindingResult,
            Model model
    ) {
        User user = registrationForm.getUser();
        Credentials credentials = registrationForm.getCredentials();
        MultipartFile file = registrationForm.getFile();

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Binding errors occurred");
            return "modifyCookForm"; // Ensure this view name matches your form view
        }

        if (!file.isEmpty()) {
            try {
                Image image = new Image();
                image.setImageData(file.getBytes());
                image.setMimeType(file.getContentType());
                user.setImage(image);
                imageService.saveImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "Error uploading image");
                return "modifyCookForm"; // Ensure this view name matches your form view
            }
        }

        // Save user and credentials
        userService.saveUser(user);
        credentialsService.saveCredentials(credentials);

        return "redirect:/"; // Redirect to appropriate success page
    }

    @GetMapping(value = "/about-us")
    public String showAboutUs(Model model) {
        return "aboutUs";
    }
}
