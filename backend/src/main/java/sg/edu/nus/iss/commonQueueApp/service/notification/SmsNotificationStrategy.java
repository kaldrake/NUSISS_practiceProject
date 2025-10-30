package sg.edu.nus.iss.commonQueueApp.service.notification;

import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SMS notification strategy implementation
 * Handles sending notifications via SMS
 * 
 * NOTE: This is a placeholder implementation. 
 * In production, integrate with SMS providers like:
 * - Twilio
 * - AWS SNS
 * - Nexmo/Vonage
 * - MessageBird
 */
@Component
public class SmsNotificationStrategy implements NotificationStrategy {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Override
    public Notification send(Customer customer, String title, String message,
                            NotificationType type, QueueEntry queueEntry) {
        // Create notification record
        Notification notification = new Notification(
                customer, type, NotificationChannel.SMS, title, message, customer.getPhone()
        );
        notification.setQueueEntry(queueEntry);
        notificationRepository.save(notification);
        
        try {
            // TODO: Integrate with SMS service provider
            // Example for Twilio:
            // Message smsMessage = Message.creator(
            //     new PhoneNumber(customer.getPhone()),
            //     new PhoneNumber(FROM_PHONE_NUMBER),
            //     message
            // ).create();
            
            // For now, just mark as sent (placeholder)
            notification.markAsSent();
            notificationRepository.save(notification);
            
            return notification;
            
        } catch (Exception e) {
            notification.markAsFailed(e.getMessage());
            notificationRepository.save(notification);
            
            return notification;
        }
    }
    
    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }
    
    @Override
    public boolean canSend(Customer customer) {
        return customer.canReceiveSmsNotifications();
    }
    
    /**
     * Resend an existing SMS notification
     * Used for retry logic
     */
    public void resend(Notification notification) throws Exception {
        // TODO: Implement SMS resend logic
        notification.markAsSent();
        notificationRepository.save(notification);
    }
}