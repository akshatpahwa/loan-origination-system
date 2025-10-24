package com.bank.loanorigination.service;

import com.bank.loanorigination.model.Agent;
import com.bank.loanorigination.model.Manager;
import com.bank.loanorigination.model.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockNotificationService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(MockNotificationService.class);

    @Override
    public void notifyAgentAssignment(Agent agent, Manager manager, Loan loan) {
        logger.info("Assigned Loan {} to Agent {} (Manager: {})",
                loan.getLoanId(), agent.getName(),
                manager != null ? manager.getName() : "None");
    }

    @Override
    public void notifyCustomerDecision(Loan loan, String message) {
        logger.info("SMS to {}: {}", loan.getCustomerPhone(), message);
    }
}
