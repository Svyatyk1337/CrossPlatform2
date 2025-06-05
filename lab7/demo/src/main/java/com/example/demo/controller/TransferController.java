package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Player;
import com.example.demo.model.Team;
import com.example.demo.model.Transfer;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TransferRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    private final TransferRepository transferRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TransferController(TransferRepository transferRepository,
                            PlayerRepository playerRepository,
                            TeamRepository teamRepository) {
        this.transferRepository = transferRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping({"", "/list"})
    public String listTransfers(Model model,
                              Authentication auth,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam(required = false) BigDecimal minFee,
                              @RequestParam(required = false) BigDecimal maxFee,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "transferDate") String sortBy,
                              @RequestParam(defaultValue = "desc") String sortDir) {

        Specification<Transfer> spec = buildSpecification(search, startDate, endDate, minFee, maxFee);
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Transfer> transferPage = transferRepository.findAll(spec, pageable);

        model.addAttribute("transfers", transferPage.getContent());
        model.addAttribute("currentPage", transferPage.getNumber());
        model.addAttribute("totalPages", transferPage.getTotalPages());
        model.addAttribute("totalItems", transferPage.getTotalElements());
        model.addAttribute("isAdmin", auth != null && auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("minFee", minFee);
        model.addAttribute("maxFee", maxFee);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("searchParam", search != null ? "&search=" + search : "");
        model.addAttribute("startDateParam", startDate != null ? "&startDate=" + startDate : "");
        model.addAttribute("endDateParam", endDate != null ? "&endDate=" + endDate : "");
        model.addAttribute("minFeeParam", minFee != null ? "&minFee=" + minFee : "");
        model.addAttribute("maxFeeParam", maxFee != null ? "&maxFee=" + maxFee : "");

        return "transfers/list";
    }

    private Specification<Transfer> buildSpecification(String search, LocalDate startDate, LocalDate endDate,
                                                    BigDecimal minFee, BigDecimal maxFee) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Eager fetch if not using Specifications with JOIN FETCH
            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                 root.fetch("player", jakarta.persistence.criteria.JoinType.LEFT);
                 root.fetch("fromTeam", jakarta.persistence.criteria.JoinType.LEFT);
                 root.fetch("toTeam", jakarta.persistence.criteria.JoinType.LEFT);
            }
            query.distinct(true);


            if (search != null && !search.trim().isEmpty()) {
                String searchTerm = "%" + search.toLowerCase() + "%";
                Predicate playerName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("player").get("name")), searchTerm);
                Predicate fromTeamName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fromTeam").get("name")), searchTerm);
                Predicate toTeamName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("toTeam").get("name")), searchTerm);
                predicates.add(criteriaBuilder.or(playerName, fromTeamName, toTeamName));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transferDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transferDate"), endDate));
            }
            if (minFee != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transferFee"), minFee));
            }
            if (maxFee != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transferFee"), maxFee));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort createSort(String sortBy, String sortDir) {
        return Sort.by("asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        Transfer newTransfer = new Transfer();
        newTransfer.setPlayer(new Player());
        newTransfer.setFromTeam(new Team());
        newTransfer.setToTeam(new Team());
        model.addAttribute("transfer", newTransfer);
        model.addAttribute("players", playerRepository.findAll(Sort.by("name")));
        model.addAttribute("teams", teamRepository.findAll(Sort.by("name")));
        return "transfers/form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        
        Transfer transfer = transferRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Невірний ID трансферу: " + id));
        
        
        if (transfer.getPlayer() == null) {
            transfer.setPlayer(new Player());
        }
        if (transfer.getFromTeam() == null) {
            transfer.setFromTeam(new Team());
        }
        if (transfer.getToTeam() == null) {
            transfer.setToTeam(new Team());
        }
        model.addAttribute("transfer", transfer);
        model.addAttribute("players", playerRepository.findAll(Sort.by("name")));
        model.addAttribute("teams", teamRepository.findAll(Sort.by("name")));
        return "transfers/form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveTransfer(@RequestParam("playerName") String playerName,
                               @RequestParam("playerPosition") String playerPosition,
                               @RequestParam("playerAge") int playerAge,
                               @RequestParam("fromTeamName") String fromTeamName,
                               @RequestParam("fromTeamCountry") String fromTeamCountry,
                               @RequestParam("toTeamName") String toTeamName,
                               @RequestParam("toTeamCountry") String toTeamCountry,
                               @Valid @ModelAttribute("transfer") Transfer transferFromForm,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (fromTeamName.equalsIgnoreCase(toTeamName) && fromTeamCountry.equalsIgnoreCase(toTeamCountry)) {
            result.rejectValue("toTeam", "error.transfer", "Команди повинні бути різними.");
        }

        if (result.hasErrors()) {
            model.addAttribute("players", playerRepository.findAll(Sort.by("name")));
            model.addAttribute("teams", teamRepository.findAll(Sort.by("name")));
            return "transfers/form";
        }

        Transfer transferToSave;
        boolean isUpdate = transferFromForm.getId() != null;

        if (isUpdate) {
            
            transferToSave = transferRepository.findById(transferFromForm.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Невірний ID трансферу для оновлення: " + transferFromForm.getId()));
        } else {
            transferToSave = new Transfer();
        }

        Team resolvedFromTeam = teamRepository.findByNameAndCountry(fromTeamName, fromTeamCountry)
                .orElseGet(() -> {
                    Team newTeam = new Team();
                    newTeam.setName(fromTeamName);
                    newTeam.setCountry(fromTeamCountry);
                    return teamRepository.save(newTeam);
                });

        Team resolvedToTeam = teamRepository.findByNameAndCountry(toTeamName, toTeamCountry)
                .orElseGet(() -> {
                    Team newTeam = new Team();
                    newTeam.setName(toTeamName);
                    newTeam.setCountry(toTeamCountry);
                    return teamRepository.save(newTeam);
                });

        Player playerToAssociate;
        if (isUpdate && transferFromForm.getPlayer() != null && transferFromForm.getPlayer().getId() != null) {
            Long existingPlayerId = transferFromForm.getPlayer().getId();
            playerToAssociate = playerRepository.findById(existingPlayerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player with ID " + existingPlayerId + " not found for update."));
            
            playerToAssociate.setName(playerName);
            playerToAssociate.setPosition(playerPosition);
            playerToAssociate.setAge(playerAge);
        } else {
            Optional<Player> foundPlayerOpt = playerRepository.findByNameContainingIgnoreCase(playerName).stream().findFirst();
            if (foundPlayerOpt.isPresent()) {
                playerToAssociate = foundPlayerOpt.get();
                playerToAssociate.setName(playerName);
                playerToAssociate.setPosition(playerPosition);
                playerToAssociate.setAge(playerAge);
            } else {
                playerToAssociate = new Player();
                playerToAssociate.setName(playerName);
                playerToAssociate.setPosition(playerPosition);
                playerToAssociate.setAge(playerAge);
            }
        }
        
        playerToAssociate.setCurrentTeam(resolvedToTeam);
        playerRepository.save(playerToAssociate);

        transferToSave.setPlayer(playerToAssociate);
        transferToSave.setFromTeam(resolvedFromTeam);
        transferToSave.setToTeam(resolvedToTeam);
        transferToSave.setTransferDate(transferFromForm.getTransferDate());
        transferToSave.setTransferFee(transferFromForm.getTransferFee());

        transferRepository.save(transferToSave);

        String successMessage = isUpdate
                ? "Трансфер успішно оновлено!"
                : "Трансфер успішно створено!";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/transfers/list";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTransfer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transferRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Трансфер успішно видалено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка при видаленні трансферу: " + e.getMessage());
        }
        return "redirect:/transfers/list";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<Transfer> searchTransfers(@RequestParam String query) {
        Specification<Transfer> spec = (root, criteriaQuery, criteriaBuilder) -> {
             if (Long.class != criteriaQuery.getResultType() && long.class != criteriaQuery.getResultType()) {
                 root.fetch("player", jakarta.persistence.criteria.JoinType.LEFT);
                 root.fetch("fromTeam", jakarta.persistence.criteria.JoinType.LEFT);
                 root.fetch("toTeam", jakarta.persistence.criteria.JoinType.LEFT);
            }
            criteriaQuery.distinct(true);
            String searchTerm = "%" + query.toLowerCase() + "%";
            Predicate playerNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("player").get("name")), searchTerm);
            Predicate fromTeamNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("fromTeam").get("name")), searchTerm);
            Predicate toTeamNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("toTeam").get("name")), searchTerm);

            return criteriaBuilder.or(playerNamePredicate, fromTeamNamePredicate, toTeamNamePredicate);
        };
        return transferRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "transferDate"));
    }

    @GetMapping("/api/filter")
    @ResponseBody
    public Page<Transfer> filterTransfers(
            @RequestParam(required = false) String playerName,
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minFee,
            @RequestParam(required = false) BigDecimal maxFee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transferDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by("asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return transferRepository.findTransfersWithFilters(
                playerName, teamName, minFee, maxFee, startDate, endDate, pageable);
    }
}