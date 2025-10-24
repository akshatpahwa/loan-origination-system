package com.bank.loanorigination.model;

import com.bank.loanorigination.enums.LoanStatus;
import com.bank.loanorigination.enums.LoanType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue
    private UUID loanId;

    private String customerName;
    private String customerPhone;
    private Double loanAmount;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Enumerated(EnumType.STRING)
    private LoanStatus applicationStatus = LoanStatus.APPLIED;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "assigned_agent_id")
    private UUID assignedAgentId;

    public Loan() {}

    // Getters and Setters
    public UUID getLoanId() {
        return loanId;
    }

    public void setLoanId(UUID loanId) {
        this.loanId = loanId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LoanStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(LoanStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(UUID assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }
}
