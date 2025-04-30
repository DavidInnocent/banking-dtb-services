package com.dtbafrica.payment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String transactionId;
    
    @Column(nullable = false)
    private String fromAccount;
    
    @Column
    private String toAccount;
    
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private TransactionStatus status;
    
    @Column
    private String description;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }
}