package com.bank.loanorigination.controller;

import com.bank.loanorigination.dto.LoanRequest;
import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.repository.LoanRepository;
import com.bank.loanorigination.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static com.bank.loanorigination.enums.LoanType.HOME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(LoanControllerTest.TestConfig.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    private Loan loan;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialize @Mock annotations
        loan = new Loan();
        loan.setCustomerName("John Doe");
        loan.setCustomerPhone("1234567890");
        loan.setLoanAmount(50000.0);
        loan.setLoanType(HOME);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public LoanRepository loanRepository() {
            return Mockito.mock(LoanRepository.class);
        }

        @Bean
        public LoanService loanService(LoanRepository loanRepository) {
            return new LoanService(loanRepository);
        }
    }

    @Test
    void createLoan() throws Exception {
        Mockito.when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        mockMvc.perform(post("/api/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "John Doe",
                                  "customerPhone": "1234567890",
                                  "loanAmount": 50000.0,
                                  "loanType": "HOME"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.loanAmount").value(50000.0));
    }

    @Test
    void getLoanStatusCounts() throws Exception {
        Mockito.when(loanRepository.findAll()).thenReturn(Collections.singletonList(loan));

        mockMvc.perform(get("/api/v1/loans/status-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.APPLIED").value(1));
    }
}
