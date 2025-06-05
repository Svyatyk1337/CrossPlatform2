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

import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public String listTeams(Model model, 
                          Authentication auth,
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) String country,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "15") int size,
                          @RequestParam(defaultValue = "name") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by("asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Team> teamPage = teamRepository.findTeamsWithFilters(search, country, pageable);
        
        model.addAttribute("teams", teamPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teamPage.getTotalPages());
        model.addAttribute("totalItems", teamPage.getTotalElements());
        model.addAttribute("isAdmin", auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("selectedCountry", country);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "teams/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("team", new Team());
        return "teams/form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невірний ID команди: " + id));
        model.addAttribute("team", team);
        return "teams/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String saveTeam(@Valid @ModelAttribute Team team,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            return "teams/form";
        }
        
        teamRepository.save(team);
        
        String successMessage = team.getId() != null ? 
            "Команда успішно оновлена!" : 
            "Команда успішно створена!";
        
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/teams";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTeam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Check if team has players before deleting
            long playerCount = teamRepository.countPlayersByTeam(id);
            if (playerCount > 0) {
                redirectAttributes.addFlashAttribute("error", 
                    "Неможливо видалити команду, оскільки до неї прикріплені гравці!");
                return "redirect:/teams";
            }
            
            teamRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Команда успішно видалена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при видаленні команди: " + e.getMessage());
        }
        return "redirect:/teams";
    }
}