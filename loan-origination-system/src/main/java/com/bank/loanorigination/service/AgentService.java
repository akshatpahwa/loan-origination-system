package com.bank.loanorigination.service;

import com.bank.loanorigination.enums.LoanStatus;
import com.bank.loanorigination.model.Agent;
import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.repository.AgentRepository;
import com.bank.loanorigination.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    private final AgentRepository agentRepository;
    private final LoanRepository loanRepository;
    private final NotificationService notificationService;

    public AgentService(AgentRepository agentRepository,
                        LoanRepository loanRepository,
                        NotificationService notificationService) {
        this.agentRepository = agentRepository;
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
    }

    /** Automatically assign loan when it goes UNDER_REVIEW */
    @Transactional
    public void assignLoanToAgent(Loan loan) {
        Optional<Agent> agentOpt = agentRepository.findNextAvailableAgent();
        Agent agent;

        if (agentOpt.isEmpty()) {
            logger.warn("No available agents to assign loan {}. Creating a new agent.", loan.getLoanId());
            agent = createNewAgent();
        } else {
            agent = agentOpt.get();
            agent.setLastAssignedAt(LocalDateTime.now());
            agent.setAvailable(false);
        }

        loan.setAssignedAgentId(agent.getId());
        loanRepository.save(loan);
        agentRepository.save(agent);

        notificationService.notifyAgentAssignment(agent, agent.getManager(), loan);

        logger.info("Loan {} assigned to agent {}", loan.getLoanId(), agent.getName());
    }

    /** Create a new agent dynamically */
    private Agent createNewAgent() {
        Agent newAgent = new Agent();
        newAgent.setId(UUID.fromString(UUID.randomUUID().toString()));
        newAgent.setName("Auto-Generated Agent");
        newAgent.setAvailable(false);
        newAgent.setCreatedAt(LocalDateTime.now());
        newAgent.setLastAssignedAt(LocalDateTime.now());
        return agentRepository.save(newAgent);
    }

    /** Agent reviews a loan and makes decision */
    @Transactional
    public Loan handleAgentDecision(String agentId, UUID loanId, String decision) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getAssignedAgentId() == null || !loan.getAssignedAgentId().toString().equals(agentId)) {
            logger.info(agentId + " is not assigned to loan " + loan.getLoanId());
            throw new RuntimeException("Agent not authorized for this loan");
        }

        if (loan.getApplicationStatus() != LoanStatus.UNDER_REVIEW) {
            throw new RuntimeException("Loan is not under review");
        }

        if (decision.equalsIgnoreCase("APPROVE")) {
            loan.setApplicationStatus(LoanStatus.APPROVED_BY_AGENT);
            notificationService.notifyCustomerDecision(loan, "Your loan has been approved by our review team!");
        } else if (decision.equalsIgnoreCase("REJECT")) {
            loan.setApplicationStatus(LoanStatus.REJECTED_BY_AGENT);
            notificationService.notifyCustomerDecision(loan, "Your loan has been rejected after review.");
        } else {
            throw new RuntimeException("Invalid decision");
        }

        loanRepository.save(loan);
        logger.info("Agent {} marked Loan {} as {}", agentId, loanId, decision);
        return loan;
    }
}
