package sg.edu.nus.iss.commonQueueApp.service.notification;

import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Email notification strategy implementation
 * Handles sending notifications via email
 */
@Component
public class EmailNotificationStrategy implements NotificationStrategy {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    private static final String FROM_EMAIL = "noreply@commonqueue.com";
    
    @Override
    public Notification send(Customer customer, String title, String message,
                            NotificationType type, QueueEntry queueEntry) {
        try {
            // Create notification record
            Notification notification = new Notification(
                    customer, type, NotificationChannel.EMAIL, title, message, customer.getEmail()
            );
            notification.setQueueEntry(queueEntry);
            notificationRepository.save(notification);
            
            // Send email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(customer.getEmail());
            mailMessage.setSubject(title);
            mailMessage.setText(message);
            mailMessage.setFrom(FROM_EMAIL);
            
            mailSender.send(mailMessage);
            
            // Mark as sent
            notification.markAsSent();
            notificationRepository.save(notification);
            
            return notification;
            
        } catch (Exception e) {
            // Log error and mark notification as failed
            Notification notification = new Notification(
                    customer, type, NotificationChannel.EMAIL, title, message, customer.getEmail()
            );
            notification.setQueueEntry(queueEntry);
            notification.markAsFailed(e.getMessage());
            notificationRepository.save(notification);
            
            return notification;
        }
    }
    
    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }
    
    @Override
    public boolean canSend(Customer customer) {
        return customer.canReceiveEmailNotifications();
    }
    
    /**
     * Resend an existing email notification
     * Used for retry logic
     */
    public void resend(Notification notification) throws Exception {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(notification.getRecipient());
        mailMessage.setSubject(notification.getTitle());
        mailMessage.setText(notification.getMessage());
        mailMessage.setFrom(FROM_EMAIL);
        
        mailSender.send(mailMessage);
        
        notification.markAsSent();
        notificationRepository.save(notification);
    }
}