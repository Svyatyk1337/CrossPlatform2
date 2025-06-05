package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TeamRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public String listPlayers(Model model, 
                            Authentication auth,
                            @RequestParam(required = false) String search,
                            @RequestParam(required = false) String position,
                            @RequestParam(required = false) Integer minAge,
                            @RequestParam(required = false) Integer maxAge,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "15") int size,
                            @RequestParam(defaultValue = "name") String sortBy,
                            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by("asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Player> playerPage = playerRepository.findPlayersWithFilters(search, position, minAge, maxAge, pageable);
        
        model.addAttribute("players", playerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", playerPage.getTotalPages());
        model.addAttribute("totalItems", playerPage.getTotalElements());
        model.addAttribute("isAdmin", auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("selectedPosition", position);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "players/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamRepository.findAll());
        return "players/form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невірний ID гравця: " + id));
        model.addAttribute("player", player);
        model.addAttribute("teams", teamRepository.findAll());
        return "players/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String savePlayer(@Valid @ModelAttribute Player player,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("teams", teamRepository.findAll());
            return "players/form";
        }
        
        playerRepository.save(player);
        
        String successMessage = player.getId() != null ? 
            "Гравець успішно оновлений!" : 
            "Гравець успішно створений!";
        
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/players";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePlayer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            playerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Гравець успішно видалений!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при видаленні гравця: " + e.getMessage());
        }
        return "redirect:/players";
    }
}