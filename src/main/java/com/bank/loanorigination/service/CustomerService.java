package com.bank.loanorigination.service;

import com.bank.loanorigination.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    private final LoanRepository loanRepository;

    public CustomerService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Map<String, Object>> getTopCustomers() {
        // Fetch top 3 customers using native query
        List<Object[]> topCustomers = loanRepository.findTopCustomers();

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : topCustomers) {
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("customerName", row[0]);
            customerData.put("customerPhone", row[1]);
            customerData.put("approvedLoanCount", row[2]);
            response.add(customerData);
        }

        return response;
    }
}
