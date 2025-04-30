package com.dtbafrica.payment_service.controller;

import com.dtbafrica.payment_service.model.Transaction;
import com.dtbafrica.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam String accountNumber,
                                               @RequestParam BigDecimal amount) {
        Transaction transaction = paymentService.deposit(accountNumber, amount);
        return ResponseEntity.ok(transaction);
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam String fromAccount,
                                                @RequestParam String toAccount,
                                                @RequestParam BigDecimal amount) {
        Transaction transaction = paymentService.transfer(fromAccount, toAccount, amount);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String transactionId) {
        Transaction transaction = paymentService.getTransaction(transactionId);
        return ResponseEntity.ok(transaction);
    }
}