package com.dtbafrica.payment_service;

import com.dtbafrica.payment_service.config.AccountClient;
import com.dtbafrica.payment_service.model.Transaction;
import com.dtbafrica.payment_service.repository.TransactionRepository;
import com.dtbafrica.payment_service.service.PaymentService;
import com.dtbafrica.store_service.config.ProfileClient;
import com.dtbafrica.store_service.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class PaymentServiceApplicationTests {
    
    @Mock
    private ProfileClient profileClient;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountClient accountClient;
    
    @Mock
    private RabbitTemplate rabbitTemplate;
    
    @InjectMocks
    private PaymentService paymentService;
    
    @Test
    void deposit_Success() {
        Account account = new Account();
        account.setAccountNumber("account1");
        account.setActive(true);
        account.setUserId(1L);
        
        when(accountClient.getAccountByNumber(anyString())).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        
        Transaction result = paymentService.deposit("account1", BigDecimal.valueOf(100));
        
        assertNotNull(result);
        assertEquals(Transaction.TransactionStatus.COMPLETED, result.getStatus());
        verify(accountClient).updateBalance(eq("account1"), eq(BigDecimal.valueOf(100)), eq("DEPOSIT"));
    }
    
    @Test
    void transfer_InsufficientFunds() {
        Account source = new Account();
        source.setAccountNumber("account1");
        source.setActive(true);
        source.setBalance(BigDecimal.valueOf(50));
        
        Account target = new Account();
        target.setAccountNumber("account2");
        target.setActive(true);
        
        when(accountClient.getAccountByNumber(eq("account1"))).thenReturn(source);
        when(accountClient.getAccountByNumber(eq("account2"))).thenReturn(target);
        
        assertThrows(RuntimeException.class, () -> {
            paymentService.transfer("account1", "account2", BigDecimal.valueOf(100));
        });
    }
}