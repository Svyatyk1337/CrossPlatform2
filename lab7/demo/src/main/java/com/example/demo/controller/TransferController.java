package com.example.demo.controller;

import com.example.demo.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransferController {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferController(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @GetMapping("/transfers")
    public String getTransfers(Model model, Authentication authentication) {
        model.addAttribute("transfers", transferRepository.findAll());
        model.addAttribute("authentication", authentication);
        return "transfers";
    }
}
