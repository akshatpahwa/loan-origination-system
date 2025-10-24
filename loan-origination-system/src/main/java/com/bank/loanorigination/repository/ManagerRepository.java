package com.bank.loanorigination.repository;
import com.bank.loanorigination.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
}
