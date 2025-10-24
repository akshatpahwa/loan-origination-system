package com.bank.loanorigination.service;

import com.bank.loanorigination.enums.LoanStatus;
import com.bank.loanorigination.model.Loan;
import com.bank.loanorigination.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executor;

@Service
@EnableScheduling
public class LoanProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(LoanProcessingService.class);

    private final LoanRepository loanRepository;
    private final Executor loanProcessorExecutor;
    private final AgentService agentService;
    private final Random random = new Random();

    public LoanProcessingService(LoanRepository loanRepository,
                                 Executor loanProcessorExecutor,
                                 AgentService agentService) {
        this.loanRepository = loanRepository;
        this.loanProcessorExecutor = loanProcessorExecutor;
        this.agentService = agentService;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleLoanProcessing() {
        Optional<Loan> loanOpt = loanRepository.findNextLoanForProcessingNative(LoanStatus.APPLIED.name());
        loanOpt.ifPresent(loan -> {
            logger.info("💼 Picked loan {} for processing", loan.getLoanId());
            loanProcessorExecutor.execute(() -> processLoan(loan));
        });
    }

    private void processLoan(Loan loan) {
        try {
            int delay = 10000 + random.nextInt(15000);
            logger.info("⏳ Processing loan {} (delay={}ms)", loan.getLoanId(), delay);
            Thread.sleep(delay);

            LoanStatus decision = decideLoanOutcome(loan);

            if (decision == LoanStatus.UNDER_REVIEW) {
                loan.setApplicationStatus(LoanStatus.UNDER_REVIEW);
                loanRepository.save(loan);
                agentService.assignLoanToAgent(loan);
                logger.info("Loan {} moved to UNDER_REVIEW and assigned to agent", loan.getLoanId());
            } else {
                loan.setApplicationStatus(decision);
                loanRepository.save(loan);
                logger.info("Loan {} processed with status {}", loan.getLoanId(), decision);
            }

        } catch (Exception e) {
            logger.error("Error processing loan {}: {}", loan.getLoanId(), e.getMessage());
        }
    }

    private LoanStatus decideLoanOutcome(Loan loan) {
        if (loan.getLoanAmount() <= 50000) {
            return LoanStatus.APPROVED_BY_SYSTEM;
        } else if (loan.getLoanAmount() >= 1000000) {
            return LoanStatus.REJECTED_BY_SYSTEM;
        } else {
            return LoanStatus.UNDER_REVIEW;
        }
    }
}
