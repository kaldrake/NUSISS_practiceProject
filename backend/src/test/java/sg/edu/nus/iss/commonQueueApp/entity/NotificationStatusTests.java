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
public class NotificationStatusTests {
    @Test
    void testEnumValues() {
        NotificationStatus pending = NotificationStatus.PENDING;
        NotificationStatus sent = NotificationStatus.SENT;
        NotificationStatus failed = NotificationStatus.FAILED;
        NotificationStatus cancelled = NotificationStatus.CANCELLED;

        assertEquals("Pending", pending.getDisplayName());
        assertEquals("Sent", sent.getDisplayName());
        assertEquals("Failed", failed.getDisplayName());
        assertEquals("Cancelled", cancelled.getDisplayName());
    }

    @Test
    void testEnumValueOf() {
        assertEquals(NotificationStatus.PENDING, NotificationStatus.valueOf("PENDING"));
        assertEquals(NotificationStatus.SENT, NotificationStatus.valueOf("SENT"));
        assertEquals(NotificationStatus.FAILED, NotificationStatus.valueOf("FAILED"));
        assertEquals(NotificationStatus.CANCELLED, NotificationStatus.valueOf("CANCELLED"));
    }

    @Test
    void testEnumValuesArray() {
        NotificationStatus[] statuses = NotificationStatus.values();
        assertEquals(4, statuses.length);
        assertArrayEquals(new NotificationStatus[]{
                NotificationStatus.PENDING,
                NotificationStatus.SENT,
                NotificationStatus.FAILED,
                NotificationStatus.CANCELLED
        }, statuses);
    }
}
