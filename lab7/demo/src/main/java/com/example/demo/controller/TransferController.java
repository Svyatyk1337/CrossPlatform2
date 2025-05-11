package com.example.demo.controller;

import com.example.demo.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final TransferRepository transferRepository;

    @GetMapping("/transfers")
    public String getTransfers(Model model) {
        model.addAttribute("transfers", transferRepository.findAll());
        return "transfers";
    }
}
