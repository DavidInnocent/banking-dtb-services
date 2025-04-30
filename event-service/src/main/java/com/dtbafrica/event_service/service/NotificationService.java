package com.dtbafrica.event_service.service;

import com.dtbafrica.event_service.model.Notification;
import com.dtbafrica.event_service.repository.NotificationRepository;
import com.dtbafrica.payment_service.model.TransactionEvent;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.store_service.config.ProfileClient;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProfileClient profileClient;

    @RabbitListener(queues = "transaction.events")
    public void handleTransactionEvent(TransactionEvent event) {
        UserInfo user = profileClient.getUser(event.getUserId());
        
        sendEmailNotification(user, event);
        sendSmsNotification(user, event);
    }

    private void sendEmailNotification(UserInfo user, TransactionEvent event) {
        String content = String.format(
                "Dear %s, your transaction %s of %s is %s. Account: %s",
                user.getName(),
                event.getType(),
                event.getAmount(),
                event.getStatus(),
                event.getFromAccount()
        );
        
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setTransactionId(event.getTransactionId());
        notification.setType("EMAIL");
        notification.setStatus("SENT");
        notification.setContent(content);
        
        notificationRepository.save(notification);
        
        // TODO implement SendMailer pending
        System.out.println("Sending email to " + user.getEmail() + ": " + content);
    }

    private void sendSmsNotification(UserInfo user, TransactionEvent event) {
        String content = String.format(
                "Transaction %s of %s is %s. Acct: %s",
                event.getType(),
                event.getAmount(),
                event.getStatus(),
                event.getFromAccount()
        );
        
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setTransactionId(event.getTransactionId());
        notification.setType("SMS");
        notification.setStatus("SENT");
        notification.setContent(content);
        
        notificationRepository.save(notification);
        
        // TODO SendMailer needed below.
        System.out.println("Sending SMS to " + user.getPhoneNumber() + ": " + content);
    }
}