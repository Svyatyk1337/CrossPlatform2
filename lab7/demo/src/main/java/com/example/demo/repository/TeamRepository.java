// TeamRepository
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    List<Team> findByNameContainingIgnoreCase(String name);
    
    List<Team> findByCountry(String country);

    Optional<Team> findByNameAndCountry(String name, String country);
    
    @Query("SELECT t FROM Team t WHERE " +
           "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:country IS NULL OR LOWER(t.country) LIKE LOWER(CONCAT('%', :country, '%')))")
    Page<Team> findTeamsWithFilters(
            @Param("name") String name,
            @Param("country") String country,
            Pageable pageable
    );
    
    @Query("SELECT COUNT(p) FROM Player p WHERE p.currentTeam.id = :teamId")
    long countPlayersByTeam(@Param("teamId") Long teamId);
}
