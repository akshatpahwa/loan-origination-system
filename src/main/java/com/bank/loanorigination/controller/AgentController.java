package com.bank.loanorigination.controller;

import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PutMapping("/{agentId}/loans/{loanId}/decision")
    public ResponseEntity<Loan> handleAgentDecision(
            @PathVariable String agentId,
            @PathVariable UUID loanId,
            @RequestBody DecisionRequest decisionRequest) {
        Loan updatedLoan = agentService.handleAgentDecision(agentId, loanId, decisionRequest.getDecision());
        return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
    }

    public static class DecisionRequest {
        private String decision;

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }
    }
}
