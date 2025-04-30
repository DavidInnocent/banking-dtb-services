package com.dtbafrica.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private Long userId;
    private String type;
    private BigDecimal amount;
    private String fromAccount;
    private String toAccount;
    private String status;
    private LocalDateTime timestamp;
}