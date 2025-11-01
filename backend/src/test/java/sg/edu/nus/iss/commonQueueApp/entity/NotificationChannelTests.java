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
public class NotificationChannelTests {
    @Test
    void testEnumValues() {
        NotificationChannel email = NotificationChannel.EMAIL;
        NotificationChannel sms = NotificationChannel.SMS;
        NotificationChannel push = NotificationChannel.PUSH;
        NotificationChannel inApp = NotificationChannel.IN_APP;

        assertEquals("Email", email.getDisplayName());
        assertEquals("SMS", sms.getDisplayName());
        assertEquals("Push Notification", push.getDisplayName());
        assertEquals("In-App Notification", inApp.getDisplayName());
    }

    @Test
    void testEnumValueOf() {
        assertEquals(NotificationChannel.EMAIL, NotificationChannel.valueOf("EMAIL"));
        assertEquals(NotificationChannel.SMS, NotificationChannel.valueOf("SMS"));
        assertEquals(NotificationChannel.PUSH, NotificationChannel.valueOf("PUSH"));
        assertEquals(NotificationChannel.IN_APP, NotificationChannel.valueOf("IN_APP"));
    }

    @Test
    void testEnumValuesArray() {
        NotificationChannel[] channels = NotificationChannel.values();
        assertEquals(4, channels.length);
        assertArrayEquals(new NotificationChannel[]{
                NotificationChannel.EMAIL,
                NotificationChannel.SMS,
                NotificationChannel.PUSH,
                NotificationChannel.IN_APP
        }, channels);
    }
}
