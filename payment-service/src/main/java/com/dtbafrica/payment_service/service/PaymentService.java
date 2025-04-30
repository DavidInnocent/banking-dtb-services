package com.dtbafrica.payment_service.service;

import com.dtbafrica.payment_service.config.AccountClient;
import com.dtbafrica.payment_service.exception.TransactionNotFoundException;
import com.dtbafrica.payment_service.model.Transaction;
import com.dtbafrica.payment_service.model.TransactionEvent;
import com.dtbafrica.payment_service.repository.TransactionRepository;
import com.dtbafrica.store_service.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountClient.getAccountByNumber(accountNumber);
        if (!account.isActive()) {
            throw new RuntimeException("Account is not activeee");
        }

        Transaction transaction = createTransaction(accountNumber, null, amount,
                Transaction.TransactionType.DEPOSIT);

        try {
            accountClient.updateBalance(accountNumber, amount, "DEPOSIT");
            
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            
            sendTransactionEvent(transaction, account.getUserId());
            
            return transaction;
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw e;
        }
    }

    @Transactional
    public Transaction transfer(String fromAccount, String toAccount, BigDecimal amount) {
        Account sourceAccount = accountClient.getAccountByNumber(fromAccount);
        Account targetAccount = accountClient.getAccountByNumber(toAccount);
        
        if (!sourceAccount.isActive() || !targetAccount.isActive()) {
            throw new RuntimeException("Account might be inactive..a");
        }
        
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds.Try againn");
        }
        Transaction transaction = createTransaction(fromAccount, toAccount, amount,
                Transaction.TransactionType.TRANSFER);

        try {
            accountClient.updateBalance(fromAccount, amount.negate(), "TRANSFER_OUT");
            accountClient.updateBalance(toAccount, amount, "TRANSFER_IN");
            
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            sendTransactionEvent(transaction, sourceAccount.getUserId());
            sendTransactionEvent(transaction, targetAccount.getUserId());
            
            return transaction;
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw e;
        }
    }

    private Transaction createTransaction(String fromAccount, String toAccount, 
                                       BigDecimal amount, Transaction.TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setDescription(type.toString());
        return transactionRepository.save(transaction);
    }
    
    @Transactional(readOnly = true)
    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found with Id : " + transactionId));
    }

    private void sendTransactionEvent(Transaction transaction, Long userId) {
        TransactionEvent event = new TransactionEvent(
                transaction.getTransactionId(),
                userId,
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getFromAccount(),
                transaction.getToAccount(),
                transaction.getStatus().toString(),
                transaction.getCreatedAt()
        );
        rabbitTemplate.convertAndSend("transaction.events", event);
    }
}