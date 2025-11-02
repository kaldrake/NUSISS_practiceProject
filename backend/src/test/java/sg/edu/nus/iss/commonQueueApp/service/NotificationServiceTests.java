/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service;

import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author junwe
 */
public class NotificationServiceTests {
    private NotificationRepository notificationRepository;
    private JavaMailSender mailSender;
    private NotificationService notificationService;

    private Customer customer;
    private Business business;
    private Queue queue;
    private QueueEntry queueEntry;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        mailSender = mock(JavaMailSender.class);

        notificationService = new NotificationService();
        // Inject mocks
        notificationService.notificationRepository = notificationRepository;
        notificationService.mailSender = mailSender;

        business = new Business();
        business.setBusinessName("Test Business");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("12345678");

        queue = new Queue();
        queue.setQueueName("Test Queue");
        queue.setBusiness(business);

        queueEntry = new QueueEntry();
        queueEntry.setCustomer(customer);
        queueEntry.setQueue(queue);
        queueEntry.setQueueNumber(5);
        queueEntry.setEstimatedWaitTimeMinutes(15);
    }

    @Test
    void testSendQueueJoinedNotification_EmailAndSmsSent() {
        // Allow email & SMS notifications
        customer.setNotificationPreference(NotificationPreference.BOTH);

        notificationService.sendQueueJoinedNotification(queueEntry);

        // Verify that repository.save was called (2 times: email + SMS)
        verify(notificationRepository, atLeast(2)).save(any(Notification.class));

        // Verify that mailSender.send was called
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendTurnApproachingNotification_SetsNotificationSent() {
        customer.setNotificationPreference(NotificationPreference.EMAIL);

        notificationService.sendTurnApproachingNotification(queueEntry);

        assertTrue(queueEntry.getNotificationSent());
        verify(notificationRepository, atLeast(1)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminderNotification_SetsReminderSent() {
        customer.setNotificationPreference(NotificationPreference.EMAIL);

        notificationService.sendReminderNotification(queueEntry);

        assertTrue(queueEntry.getReminderSent());
        verify(notificationRepository, atLeast(1)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testMarkNotificationAsRead() {
        Notification notification = mock(Notification.class);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.markNotificationAsRead(1L);

        verify(notification).markAsRead();
        verify(notificationRepository).save(notification);
    }

    @Test
    void testMarkNotificationAsRead_ThrowsWhenNotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> notificationService.markNotificationAsRead(1L));
        assertEquals("Notification not found", exception.getMessage());
    }

    @Test
    void testGetCustomerNotifications() {
        when(notificationRepository.findByCustomerOrderByCreatedAtDesc(any()))
                .thenReturn(List.of(new Notification(), new Notification()));

        List<Notification> notifications = notificationService.getCustomerNotifications(customer.getId());
        assertEquals(2, notifications.size());
        verify(notificationRepository).findByCustomerOrderByCreatedAtDesc(any(Customer.class));
    }

    @Test
    void testGetUnreadNotificationsCount() {
        when(notificationRepository.countUnreadNotificationsByCustomerId(customer.getId())).thenReturn(5L);

        Long count = notificationService.getUnreadNotificationsCount(customer.getId());
        assertEquals(5L, count);
    }

    @Test
    void testRetryFailedNotifications() {
        Notification notif = mock(Notification.class);
        when(notificationRepository.findFailedNotificationsForRetry()).thenReturn(List.of(notif));

        notificationService.retryFailedNotifications();

        verify(notif).incrementRetryCount();
        verify(notif).setStatus(NotificationStatus.PENDING);
        verify(notificationRepository).save(notif);
    }

    @Test
    void testCleanupOldNotifications() {
        Notification oldNotif = mock(Notification.class);
        when(notificationRepository.findPendingNotificationsOlderThan(any())).thenReturn(List.of(oldNotif));

        notificationService.cleanupOldNotifications();

        verify(oldNotif).setStatus(NotificationStatus.CANCELLED);
        verify(notificationRepository).saveAll(List.of(oldNotif));
    }
    
    @Test
    void testSendTurnReadyNotification_EmailAndSmsSent() {
        customer.setNotificationPreference(NotificationPreference.BOTH);

        notificationService.sendTurnReadyNotification(queueEntry);

        // Verify that notification repository saves the notification records
        verify(notificationRepository, atLeast(2)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendQueueCancellationNotification_EmailAndSmsSent() {
        customer.setNotificationPreference(NotificationPreference.BOTH);

        notificationService.sendQueueCancellationNotification(queueEntry);

        verify(notificationRepository, atLeast(2)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendFeedbackRequestNotification_EmailOnly() {
        customer.setNotificationPreference(NotificationPreference.BOTH);

        notificationService.sendFeedbackRequestNotification(queueEntry);

        verify(notificationRepository, atLeast(1)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendQueueDelayNotification_EmailAndSmsSent() {
        customer.setNotificationPreference(NotificationPreference.BOTH);

        int additionalMinutes = 10;
        notificationService.sendQueueDelayNotification(queueEntry, additionalMinutes);

        verify(notificationRepository, atLeast(2)).save(any(Notification.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
