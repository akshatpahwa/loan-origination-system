package com.bank.loanorigination.service;

import com.bank.loanorigination.dto.LoanRequest;
import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.enums.LoanStatus;
import com.bank.loanorigination.repository.LoanRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Transactional
    public Loan createLoan(LoanRequest request) {
        Loan loan = new Loan();
        loan.setCustomerName(request.getCustomerName());
        loan.setCustomerPhone(request.getCustomerPhone());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setLoanType(request.getLoanType());
        loan.setApplicationStatus(LoanStatus.APPLIED);
        return loanRepository.save(loan);
    }

    public Map<String, Long> getLoanStatusCounts() {
        return loanRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        loan -> loan.getApplicationStatus().name(),
                        Collectors.counting()
                ));
    }

    public Page<Loan> getLoansByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (status == null || status.isBlank()) {
            // No filter → return all loans paginated
            return loanRepository.findAll(pageable);
        } else {
            return loanRepository.findByApplicationStatus(status, pageable);
        }
    }
}
