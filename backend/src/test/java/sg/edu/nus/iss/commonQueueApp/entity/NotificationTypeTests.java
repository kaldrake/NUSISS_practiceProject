/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class NotificationTypeTests {
    @Test
    void testNotificationStatus() {
        assertEquals("Pending", NotificationStatus.PENDING.getDisplayName());
        assertEquals("Sent", NotificationStatus.SENT.getDisplayName());
        assertEquals("Failed", NotificationStatus.FAILED.getDisplayName());
        assertEquals("Cancelled", NotificationStatus.CANCELLED.getDisplayName());

        assertEquals(NotificationStatus.PENDING, NotificationStatus.valueOf("PENDING"));
        assertEquals(4, NotificationStatus.values().length);
    }

    @Test
    void testNotificationChannel() {
        assertEquals("Email", NotificationChannel.EMAIL.getDisplayName());
        assertEquals("SMS", NotificationChannel.SMS.getDisplayName());
        assertEquals("Push Notification", NotificationChannel.PUSH.getDisplayName());
        assertEquals("In-App Notification", NotificationChannel.IN_APP.getDisplayName());

        assertEquals(NotificationChannel.EMAIL, NotificationChannel.valueOf("EMAIL"));
        assertEquals(4, NotificationChannel.values().length);
    }

    @Test
    void testNotificationPreference() {
        assertEquals("Email Only", NotificationPreference.EMAIL.getDisplayName());
        assertEquals("SMS Only", NotificationPreference.SMS.getDisplayName());
        assertEquals("Email and SMS", NotificationPreference.BOTH.getDisplayName());
        assertEquals("No Notifications", NotificationPreference.NONE.getDisplayName());

        assertEquals(NotificationPreference.EMAIL, NotificationPreference.valueOf("EMAIL"));
        assertEquals(4, NotificationPreference.values().length);
    }

    @Test
    void testNotificationType() {
        assertEquals("Queue Joined", NotificationType.QUEUE_JOINED.getDisplayName());
        assertEquals("Your Turn is Approaching", NotificationType.TURN_APPROACHING.getDisplayName());
        assertEquals("Your Turn is Ready", NotificationType.TURN_READY.getDisplayName());
        assertEquals("Queue Cancelled", NotificationType.QUEUE_CANCELLED.getDisplayName());
        assertEquals("Queue Delayed", NotificationType.QUEUE_DELAYED.getDisplayName());
        assertEquals("Reminder", NotificationType.REMINDER.getDisplayName());
        assertEquals("Feedback Request", NotificationType.FEEDBACK_REQUEST.getDisplayName());

        assertEquals(NotificationType.QUEUE_JOINED, NotificationType.valueOf("QUEUE_JOINED"));
        assertEquals(7, NotificationType.values().length);
    }
}
