package com.dtbafrica.store_service.service;

import com.dtbafrica.store_service.config.ProfileClient;
import com.dtbafrica.store_service.model.Account;
import com.dtbafrica.store_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ProfileClient profileClient;

    @Transactional
    public Account createAccount(Long userId, Account.AccountType type) {
        // Verify user exists
        profileClient.getUser(userId);
        
        Account account = new Account();
        account.setUserId(userId);
        account.setType(type);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public Account updateAccountStatus(Long id, boolean active) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(active);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return "AC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}