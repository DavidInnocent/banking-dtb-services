package com.dtbafrica.payment_service.config;

import com.dtbafrica.store_service.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {
    @GetMapping("/api/accounts/{accountNumber}")
    Account getAccountByNumber(@PathVariable String accountNumber);
    @PostMapping("/api/accounts/{accountNumber}/balance")
    void updateBalance(@PathVariable String accountNumber, 
                     @RequestParam BigDecimal amount,
                     @RequestParam String operation);
}