package com.bank.loanorigination.controller;

import com.bank.loanorigination.dto.LoanRequest;
import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequest request) {
        Loan createdLoan = loanService.createLoan(request);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping("/status-count")
    public ResponseEntity<Map<String, Long>> getLoanStatusCount() {
        Map<String, Long> statusCounts = loanService.getLoanStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping
    public ResponseEntity<Page<Loan>> getLoansByStatus(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Loan> loans = loanService.getLoansByStatus(status, page, size);
        return ResponseEntity.ok(loans);
    }
}
