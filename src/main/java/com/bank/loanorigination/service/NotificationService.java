package com.bank.loanorigination.service;

import com.bank.loanorigination.model.Agent;
import com.bank.loanorigination.model.Manager;
import com.bank.loanorigination.model.Loan;

public interface NotificationService {
    void notifyAgentAssignment(Agent agent, Manager manager, Loan loan);
    void notifyCustomerDecision(Loan loan, String message);
}
