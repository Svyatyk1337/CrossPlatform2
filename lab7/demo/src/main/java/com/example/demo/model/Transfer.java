package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString; // Import if using Exclude

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @ToString.Exclude 
    private Player player;

    @ManyToOne
    @JoinColumn(name = "from_team_id")
    @ToString.Exclude 
    private Team fromTeam;

    @ManyToOne
    @JoinColumn(name = "to_team_id")
    @ToString.Exclude 
    private Team toTeam;

    private LocalDate transferDate;
    private BigDecimal transferFee;
}