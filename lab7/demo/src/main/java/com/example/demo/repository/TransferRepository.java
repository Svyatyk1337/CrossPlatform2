package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>, JpaSpecificationExecutor<Transfer> {
    
    // Existing methods...
    List<Transfer> findByPlayerNameContainingIgnoreCase(String playerName);
    
    @Query("SELECT t FROM Transfer t WHERE " +
           "LOWER(t.fromTeam.name) LIKE LOWER(CONCAT('%', :teamName, '%')) OR " +
           "LOWER(t.toTeam.name) LIKE LOWER(CONCAT('%', :teamName, '%'))")
    List<Transfer> findByTeamName(@Param("teamName") String teamName);
    
    List<Transfer> findByTransferDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Transfer> findByTransferFeeBetween(BigDecimal minFee, BigDecimal maxFee);
    
    @Query("SELECT t FROM Transfer t ORDER BY t.transferFee DESC")
    List<Transfer> findTopTransfersByFee();
    
    @Query("SELECT t FROM Transfer t ORDER BY t.transferDate DESC")
    List<Transfer> findRecentTransfers();
    
    List<Transfer> findByFromTeamId(Long teamId);
    
    List<Transfer> findByToTeamId(Long teamId);
    
    // Enhanced method with Pageable support
    @Query("SELECT t FROM Transfer t WHERE " +
           "(:playerName IS NULL OR LOWER(t.player.name) LIKE LOWER(CONCAT('%', :playerName, '%'))) AND " +
           "(:teamName IS NULL OR LOWER(t.fromTeam.name) LIKE LOWER(CONCAT('%', :teamName, '%')) OR " +
           "                     LOWER(t.toTeam.name) LIKE LOWER(CONCAT('%', :teamName, '%'))) AND " +
           "(:minFee IS NULL OR t.transferFee >= :minFee) AND " +
           "(:maxFee IS NULL OR t.transferFee <= :maxFee) AND " +
           "(:startDate IS NULL OR t.transferDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.transferDate <= :endDate)")
    Page<Transfer> findTransfersWithFilters(
            @Param("playerName") String playerName,
            @Param("teamName") String teamName,
            @Param("minFee") BigDecimal minFee,
            @Param("maxFee") BigDecimal maxFee,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
    
    @Query("SELECT COUNT(t) FROM Transfer t WHERE t.transferDate >= :date")
    long countTransfersSinceDate(@Param("date") LocalDate date);
    
    @Query("SELECT SUM(t.transferFee) FROM Transfer t WHERE t.transferDate >= :date")
    BigDecimal sumTransferFeesSinceDate(@Param("date") LocalDate date);
    
    @Query("SELECT AVG(t.transferFee) FROM Transfer t")
    BigDecimal getAverageTransferFee();
    
    @Query("SELECT t FROM Transfer t ORDER BY t.transferFee DESC")
    Page<Transfer> findTopExpensiveTransfers(Pageable pageable);
    
    @Query("SELECT t FROM Transfer t WHERE t.fromTeam.id = :teamId OR t.toTeam.id = :teamId ORDER BY t.transferDate DESC")
    Page<Transfer> findRecentTransfersByTeam(@Param("teamId") Long teamId, Pageable pageable);
    
    @Query("SELECT t FROM Transfer t WHERE t.player.id = :playerId ORDER BY t.transferDate DESC")
    List<Transfer> findTransfersByPlayer(@Param("playerId") Long playerId);
    
    @Query("SELECT COUNT(t) FROM Transfer t WHERE t.fromTeam.id = :teamId OR t.toTeam.id = :teamId")
    long countTransfersByTeam(@Param("teamId") Long teamId);
    
    @Query("SELECT t FROM Transfer t WHERE t.transferDate BETWEEN :startDate AND :endDate " +
           "AND t.transferFee >= :minFee ORDER BY t.transferFee DESC")
    List<Transfer> findExpensiveTransfersInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("minFee") BigDecimal minFee
    );
    @Query("SELECT t FROM Transfer t " +
       "LEFT JOIN FETCH t.player p " +
       "LEFT JOIN FETCH t.fromTeam ft " +
       "LEFT JOIN FETCH t.toTeam tt " +
       "WHERE t.id = :id")
Optional<Transfer> findByIdWithDetails(@Param("id") Long id);
}
