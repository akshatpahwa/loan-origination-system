package com.bank.loanorigination.dto;

import com.bank.loanorigination.enums.LoanType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LoanRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String customerPhone;

    @NotNull(message = "Loan amount is required")
    @Min(value = 1, message = "Loan amount must be positive")
    private Double loanAmount;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    // Getters and Setters
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
}
