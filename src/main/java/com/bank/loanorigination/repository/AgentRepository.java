package com.bank.loanorigination.repository;

import com.bank.loanorigination.model.Agent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {

    // Find least recently assigned available agent
    @Query("SELECT a FROM Agent a WHERE a.isAvailable = true ORDER BY a.lastAssignedAt NULLS FIRST")
    Optional<Agent> findNextAvailableAgent();
}
