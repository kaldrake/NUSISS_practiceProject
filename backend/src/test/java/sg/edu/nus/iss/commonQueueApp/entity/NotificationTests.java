/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class NotificationTests {
    private Notification notification;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com", "+6512345678");
        notification = new Notification();
    }

    @Test
    void testSettersAndGetters() {
        notification.setId(1L);
        notification.setCustomer(customer);
        notification.setNotificationType(NotificationType.TURN_APPROACHING);
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setTitle("Queue Update");
        notification.setMessage("Your queue number is ready");
        notification.setRecipient("john@example.com");
        notification.setStatus(NotificationStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();
        notification.setSentAt(now);
        notification.setReadAt(now);
        notification.setErrorMessage("Error occurred");
        notification.setRetryCount(2);
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);

        assertEquals(1L, notification.getId());
        assertEquals(customer, notification.getCustomer());
        assertEquals(NotificationType.TURN_APPROACHING, notification.getNotificationType());
        assertEquals(NotificationChannel.EMAIL, notification.getChannel());
        assertEquals("Queue Update", notification.getTitle());
        assertEquals("Your queue number is ready", notification.getMessage());
        assertEquals("john@example.com", notification.getRecipient());
        assertEquals(NotificationStatus.PENDING, notification.getStatus());
        assertEquals(now, notification.getSentAt());
        assertEquals(now, notification.getReadAt());
        assertEquals("Error occurred", notification.getErrorMessage());
        assertEquals(2, notification.getRetryCount());
        assertEquals(now, notification.getCreatedAt());
        assertEquals(now, notification.getUpdatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        Notification n = new Notification(customer,
                NotificationType.TURN_READY,
                NotificationChannel.SMS,
                "Title",
                "Message",
                "+6512345678");

        assertEquals(customer, n.getCustomer());
        assertEquals(NotificationType.TURN_READY, n.getNotificationType());
        assertEquals(NotificationChannel.SMS, n.getChannel());
        assertEquals("Title", n.getTitle());
        assertEquals("Message", n.getMessage());
        assertEquals("+6512345678", n.getRecipient());
        assertEquals(NotificationStatus.PENDING, n.getStatus()); // default
    }

    @Test
    void testMarkAsSent() {
        notification.markAsSent();
        assertEquals(NotificationStatus.SENT, notification.getStatus());
        assertNotNull(notification.getSentAt());
    }

    @Test
    void testMarkAsFailed() {
        notification.markAsFailed("Failed due to network error");
        assertEquals(NotificationStatus.FAILED, notification.getStatus());
        assertEquals("Failed due to network error", notification.getErrorMessage());
    }

    @Test
    void testMarkAsRead() {
        notification.markAsRead();
        assertNotNull(notification.getReadAt());
    }

    @Test
    void testIncrementRetryCount() {
        int initialCount = notification.getRetryCount();
        notification.incrementRetryCount();
        assertEquals(initialCount + 1, notification.getRetryCount());
    }

    @Test
    void testCanRetry() {
        // Not failed => cannot retry
        notification.setStatus(NotificationStatus.PENDING);
        notification.setRetryCount(0);
        assertFalse(notification.canRetry());

        // Failed and retry < 3 => can retry
        notification.setStatus(NotificationStatus.FAILED);
        notification.setRetryCount(2);
        assertTrue(notification.canRetry());

        // Failed but retry >= 3 => cannot retry
        notification.setRetryCount(3);
        assertFalse(notification.canRetry());
    }

    @Test
    void testIsPending() {
        notification.setStatus(NotificationStatus.PENDING);
        assertTrue(notification.isPending());

        notification.setStatus(NotificationStatus.SENT);
        assertFalse(notification.isPending());
    }
}
