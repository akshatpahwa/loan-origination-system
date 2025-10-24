package com.bank.loanorigination;

import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bank.loanorigination.controller.LoanController;

@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    private Loan loan;

    @BeforeEach
    void setup() {
        loan = new Loan();
        loan.setLoanId(UUID.randomUUID());
        loan.setCustomerName("Akshat Pahwa");
        loan.setCustomerPhone("9876543210");
        loan.setLoanAmount(60000.0);
        loan.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSubmitLoanApplication() throws Exception {
        // Mock behavior
        Mockito.when(loanService.createLoan(any())).thenReturn(loan);

        mockMvc.perform(post("/api/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Akshat Pahwa",
                                  "customerPhone": "9876543210",
                                  "loanAmount": 60000,
                                  "loanType": "HOME"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Akshat Pahwa"))
                .andExpect(jsonPath("$.loanAmount").value(60000.0));
    }

    @Test
    void testGetLoanStatusCount() throws Exception {
        Map<String, Long> mockData = Map.of(
                "APPROVED_BY_SYSTEM", 3L,
                "UNDER_REVIEW", 2L
        );

        Mockito.when(loanService.getLoanStatusCounts()).thenReturn(mockData);

        mockMvc.perform(get("/api/v1/loans/status-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.APPROVED_BY_SYSTEM").value(3))
                .andExpect(jsonPath("$.UNDER_REVIEW").value(2));
    }

    @Test
    void testGetLoansByStatusWithPagination() throws Exception {
        Page<Loan> mockPage = new PageImpl<>(List.of(loan));
        Mockito.when(loanService.getLoansByStatus(eq("APPLIED"), eq(0), eq(10))).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/loans")
                        .param("status", "APPLIED")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerName").value("Akshat Pahwa"))
                .andExpect(jsonPath("$.content[0].loanAmount").value(60000.0));
    }
}
