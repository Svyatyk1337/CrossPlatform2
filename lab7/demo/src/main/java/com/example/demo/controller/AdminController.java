package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("authentication", authentication);
        return "admin/dashboard";
    }
}
