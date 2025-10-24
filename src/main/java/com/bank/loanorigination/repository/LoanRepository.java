package com.bank.loanorigination.repository;

import com.bank.loanorigination.model.Loan;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

    // Existing query — keep this for multi-threaded loan processing
    @Query(value = """
        SELECT * FROM loans
        WHERE application_status = :status
        ORDER BY created_at
        FOR UPDATE SKIP LOCKED
        LIMIT 1
        """, nativeQuery = true)
    Optional<Loan> findNextLoanForProcessingNative(@Param("status") String status);


    // New query — for Top Customers API
    @Query(value = """
        SELECT customer_name, customer_phone, COUNT(*) AS approved_count
        FROM loans
        WHERE application_status IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT')
        GROUP BY customer_name, customer_phone
        ORDER BY approved_count DESC
        LIMIT 3
        """, nativeQuery = true)
    List<Object[]> findTopCustomers();

    @Query("SELECT l FROM Loan l WHERE (:status IS NULL OR l.applicationStatus = :status)")
    Page<Loan> findByApplicationStatus(@Param("status") String status, Pageable pageable);
}
