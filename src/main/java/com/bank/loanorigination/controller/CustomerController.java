package com.bank.loanorigination.controller;

import com.bank.loanorigination.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
        return ResponseEntity.ok(customerService.getTopCustomers());
    }
}
