package com.dtbafrica.store_service.controller;

import com.dtbafrica.store_service.model.Account;
import com.dtbafrica.store_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam Long userId,
                                                 @RequestParam Account.AccountType type) {
        Account account = accountService.createAccount(userId, type);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id, 
                                            @RequestParam Long userId) {
        Account account = accountService.getAccount(id, userId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Account> updateStatus(@PathVariable Long id, 
                                              @RequestParam boolean active) {
        Account account = accountService.updateAccountStatus(id, active);
        return ResponseEntity.ok(account);
    }
}