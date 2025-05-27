package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("authentication", authentication);
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "registered", required = false) String registered,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Невірне ім'я користувача або пароль!");
        }
        if (logout != null) {
            model.addAttribute("logout", "Ви успішно вийшли з системи!");
        }
        if (registered != null) {
            model.addAttribute("registered", "Реєстрація пройшла успішно! Тепер ви можете увійти.");
        }
        
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               @RequestParam String confirmPassword,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "register";
        }
        
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Паролі не співпадають!");
            return "register";
        }
        
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Користувач з таким ім'ям вже існує!");
            return "register";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Користувач з таким email вже існує!");
            return "register";
        }
        
        user.setRole(User.Role.USER);
        userService.saveUser(user);
        
        redirectAttributes.addAttribute("registered", "true");
        return "redirect:/login";
    }
}