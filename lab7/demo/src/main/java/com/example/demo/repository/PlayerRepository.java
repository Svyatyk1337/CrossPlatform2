// PlayerRepository
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    List<Player> findByNameContainingIgnoreCase(String name);
    
    List<Player> findByPosition(String position);
    
    List<Player> findByAgeBetween(int minAge, int maxAge);
    
    List<Player> findByCurrentTeamId(Long teamId);
    
    @Query("SELECT p FROM Player p WHERE p.currentTeam IS NULL")
    List<Player> findFreePlayers();
    
    @Query("SELECT p FROM Player p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:position IS NULL OR p.position = :position) AND " +
           "(:minAge IS NULL OR p.age >= :minAge) AND " +
           "(:maxAge IS NULL OR p.age <= :maxAge)")
    Page<Player> findPlayersWithFilters(
            @Param("name") String name,
            @Param("position") String position,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            Pageable pageable
    );
}
