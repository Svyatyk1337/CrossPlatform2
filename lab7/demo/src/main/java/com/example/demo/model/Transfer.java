package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "from_team_id")
    private Team fromTeam;

    @ManyToOne
    @JoinColumn(name = "to_team_id")
    private Team toTeam;

    private LocalDate transferDate;

    private BigDecimal transferFee;
}
