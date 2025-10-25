package sg.edu.nus.iss.commonQueueApp.service;

import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;
import sg.edu.nus.iss.commonQueueApp.service.notification.EmailNotificationStrategy;
import sg.edu.nus.iss.commonQueueApp.service.notification.NotificationStrategy;
import sg.edu.nus.iss.commonQueueApp.service.notification.NotificationStrategyFactory;
import sg.edu.nus.iss.commonQueueApp.service.notification.SmsNotificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for handling notifications using Strategy Pattern
 * 
 * Benefits of Strategy Pattern:
 * - Easy to add new notification channels (WhatsApp, Push, Telegram)
 * - Each channel is independent and can be tested separately
 * - Cleaner code with separated concerns
 * - No if-else chains for channel selection
 */
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private NotificationStrategyFactory strategyFactory;
    
    @Autowired
    private EmailNotificationStrategy emailStrategy;
    
    @Autowired
    private SmsNotificationStrategy smsStrategy;

    /**
     * Send queue joined confirmation notification
     */
    @Async
    public void sendQueueJoinedNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Queue Joined Successfully";
        String message = String.format(
                "You have joined the queue for %s at %s. Your number is %d. Estimated wait time: %d minutes.",
                queue.getQueueName(),
                queue.getBusiness().getBusinessName(),
                queueEntry.getQueueNumber(),
                queueEntry.getEstimatedWaitTimeMinutes()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.QUEUE_JOINED, queueEntry);
    }

    /**
     * Send turn approaching notification (15 minutes before)
     */
    @Async
    public void sendTurnApproachingNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Your Turn is Approaching";
        String message = String.format(
                "Your turn at %s (%s) is approaching. You are number %d. Please be ready in approximately 15 minutes.",
                queue.getBusiness().getBusinessName(),
                queue.getQueueName(),
                queueEntry.getQueueNumber()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.TURN_APPROACHING, queueEntry);

        // Mark notification as sent
        queueEntry.setNotificationSent(true);
    }

    /**
     * Send turn ready notification
     */
    @Async
    public void sendTurnReadyNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Your Turn is Ready!";
        String message = String.format(
                "It's your turn at %s (%s). Please proceed to the service counter. Your number is %d.",
                queue.getBusiness().getBusinessName(),
                queue.getQueueName(),
                queueEntry.getQueueNumber()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.TURN_READY, queueEntry);
    }

    /**
     * Send queue cancellation notification
     */
    @Async
    public void sendQueueCancellationNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Queue Cancelled";
        String message = String.format(
                "The queue for %s at %s has been cancelled. We apologize for any inconvenience.",
                queue.getQueueName(),
                queue.getBusiness().getBusinessName()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.QUEUE_CANCELLED, queueEntry);
    }

    /**
     * Send feedback request notification
     */
    @Async
    public void sendFeedbackRequestNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "How was your experience?";
        String message = String.format(
                "Thank you for visiting %s. Please take a moment to rate your experience and help us improve our service.",
                queue.getBusiness().getBusinessName()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.FEEDBACK_REQUEST, queueEntry);
    }

    /**
     * Send reminder notification (5 minutes before)
     */
    @Async
    public void sendReminderNotification(QueueEntry queueEntry) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Reminder: Your Turn is Coming Soon";
        String message = String.format(
                "Reminder: Your turn at %s (%s) is in approximately 5 minutes. Number %d.",
                queue.getBusiness().getBusinessName(),
                queue.getQueueName(),
                queueEntry.getQueueNumber()
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.REMINDER, queueEntry);

        // Mark reminder as sent
        queueEntry.setReminderSent(true);
    }

    /**
     * Send queue delay notification
     */
    @Async
    public void sendQueueDelayNotification(QueueEntry queueEntry, int additionalMinutes) {
        Customer customer = queueEntry.getCustomer();
        Queue queue = queueEntry.getQueue();

        String title = "Queue Delay Notice";
        String message = String.format(
                "There is a delay in the queue at %s (%s). Your estimated wait time has increased by %d minutes. We apologize for the inconvenience.",
                queue.getBusiness().getBusinessName(),
                queue.getQueueName(),
                additionalMinutes
        );

        sendNotificationToCustomer(customer, title, message, NotificationType.QUEUE_DELAYED, queueEntry);
    }

    /**
     * Core method to send notification to customer through all their preferred channels
     * Uses Strategy Pattern to delegate to appropriate notification strategies
     */
    private void sendNotificationToCustomer(Customer customer, String title, String message,
                                           NotificationType type, QueueEntry queueEntry) {
        // Send through all applicable channels based on customer preferences
        for (NotificationStrategy strategy : strategyFactory.getAllStrategies().values()) {
            if (strategy.canSend(customer)) {
                strategy.send(customer, title, message, type, queueEntry);
            }
        }
    }

    /**
     * Process pending notifications (scheduled task)
     */
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository
                .findByStatusOrderByCreatedAtAsc(NotificationStatus.PENDING);

        for (Notification notification : pendingNotifications) {
            try {
                NotificationStrategy strategy = strategyFactory.getStrategy(notification.getChannel());
                
                // Resend using appropriate strategy
                if (strategy instanceof EmailNotificationStrategy) {
                    ((EmailNotificationStrategy) strategy).resend(notification);
                } else if (strategy instanceof SmsNotificationStrategy) {
                    ((SmsNotificationStrategy) strategy).resend(notification);
                }
                
            } catch (Exception e) {
                notification.markAsFailed(e.getMessage());
                notificationRepository.save(notification);
            }
        }
    }

    /**
     * Retry failed notifications
     */
    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository.findFailedNotificationsForRetry();

        for (Notification notification : failedNotifications) {
            notification.incrementRetryCount();
            notification.setStatus(NotificationStatus.PENDING);
            notificationRepository.save(notification);
        }
    }

    /**
     * Get customer notifications
     */
    public List<Notification> getCustomerNotifications(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return notificationRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    /**
     * Mark notification as read
     */
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * Get unread notifications count for customer
     */
    public Long getUnreadNotificationsCount(Long customerId) {
        return notificationRepository.countUnreadNotificationsByCustomerId(customerId);
    }

    /**
     * Send bulk notifications to all customers in queue
     */
    @Async
    public void sendBulkQueueNotification(Long queueId, String title, String message) {
        // This would be used for emergency notifications or general announcements
        // Implementation would fetch all active queue entries and send notifications
    }

    /**
     * Clean up old notifications (scheduled task)
     */
    public void cleanupOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<Notification> oldNotifications = notificationRepository
                .findPendingNotificationsOlderThan(cutoffDate);

        // Mark old pending notifications as cancelled
        for (Notification notification : oldNotifications) {
            notification.setStatus(NotificationStatus.CANCELLED);
        }

        notificationRepository.saveAll(oldNotifications);
    }
}