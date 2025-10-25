package sg.edu.nus.iss.commonQueueApp.service.notification;

import sg.edu.nus.iss.commonQueueApp.entity.*;

/**
 * Strategy interface for sending notifications through different channels
 * 
 * This allows easy extension to support new notification channels
 * (WhatsApp, Push Notifications, Telegram, etc.) without modifying existing code
 */
public interface NotificationStrategy {
    
    /**
     * Send a notification to the customer
     * 
     * @param customer The customer to notify
     * @param title Notification title
     * @param message Notification message
     * @param type Type of notification
     * @param queueEntry Related queue entry
     * @return The created Notification entity
     */
    Notification send(Customer customer, String title, String message, 
                     NotificationType type, QueueEntry queueEntry);
    
    /**
     * Get the notification channel this strategy handles
     * 
     * @return The notification channel
     */
    NotificationChannel getChannel();
    
    /**
     * Check if this strategy can send to the given customer
     * 
     * @param customer The customer to check
     * @return true if notification can be sent, false otherwise
     */
    boolean canSend(Customer customer);
}