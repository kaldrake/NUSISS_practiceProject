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
public class QueueEntryTests {
    private Queue queue;
    private Customer customer;
    private QueueEntry entry;

    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com", "12345678");
        queue = new Queue("Test Queue", new Business("Test Biz", "biz@example.com", "pass"));
        queue.setAvgServiceTimeMinutes(10);

        // Create entry with queue number 1
        entry = new QueueEntry(queue, customer, 1);
        queue.addQueueEntry(entry);
    }

    @Test
    void testConstructor() {
        assertEquals(queue, entry.getQueue());
        assertEquals(customer, entry.getCustomer());
        assertEquals(1, entry.getQueueNumber());
        assertEquals(0, entry.getEstimatedWaitTimeMinutes());
        assertEquals(QueueEntryStatus.WAITING, entry.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        entry.setQueueNumber(5);
        assertEquals(5, entry.getQueueNumber());

        entry.setPriorityLevel(2);
        assertEquals(2, entry.getPriorityLevel());

        entry.setNotes("Test note");
        assertEquals("Test note", entry.getNotes());

        entry.setNotificationSent(true);
        assertTrue(entry.getNotificationSent());

        entry.setReminderSent(true);
        assertTrue(entry.getReminderSent());
    }

    @Test
    void testUpdateEstimatedWaitTime() {
        QueueEntry secondEntry = new QueueEntry(queue, new Customer("Jane", null, null), 2);
        queue.addQueueEntry(secondEntry);

        // First entry's estimated wait should be 0
        entry.updateEstimatedWaitTime();
        assertEquals(0, entry.getEstimatedWaitTimeMinutes());

        // Second entry's estimated wait should be 10
        secondEntry.updateEstimatedWaitTime();
        assertEquals(10, secondEntry.getEstimatedWaitTimeMinutes());
    }

    @Test
    void testActualWaitTimeMinutes() throws InterruptedException {
        entry.setJoinedAt(LocalDateTime.now().minusMinutes(5));
        entry.setCalledAt(LocalDateTime.now());

        Long actualWait = entry.getActualWaitTimeMinutes();
        assertTrue(actualWait >= 4 && actualWait <= 5);
    }

    @Test
    void testServiceTimeMinutes() throws InterruptedException {
        entry.setCalledAt(LocalDateTime.now().minusMinutes(5));
        entry.setServedAt(LocalDateTime.now());

        Long serviceTime = entry.getServiceTimeMinutes();
        assertTrue(serviceTime >= 4 && serviceTime <= 5);
    }

    @Test
    void testStatusTransitions() {
        entry.markAsCalled();
        assertEquals(QueueEntryStatus.CALLED, entry.getStatus());
        assertNotNull(entry.getCalledAt());

        entry.markAsServed();
        assertEquals(QueueEntryStatus.SERVED, entry.getStatus());
        assertNotNull(entry.getServedAt());

        entry.markAsCancelled();
        assertEquals(QueueEntryStatus.CANCELLED, entry.getStatus());
        assertNotNull(entry.getCancelledAt());

        entry.markAsNoShow();
        assertEquals(QueueEntryStatus.NO_SHOW, entry.getStatus());
        assertNotNull(entry.getNoShowAt());
    }

    @Test
    void testIsActive() {
        entry.setStatus(QueueEntryStatus.WAITING);
        assertTrue(entry.isActive());

        entry.setStatus(QueueEntryStatus.CALLED);
        assertTrue(entry.isActive());

        entry.setStatus(QueueEntryStatus.SERVED);
        assertFalse(entry.isActive());
    }

    @Test
    void testGetPositionInQueue() {
        QueueEntry secondEntry = new QueueEntry(queue, new Customer("Jane", null, null), 2);
        queue.addQueueEntry(secondEntry);

        assertEquals(1, entry.getPositionInQueue());
        assertEquals(2, secondEntry.getPositionInQueue());
    }

    @Test
    void testShouldNotifyAndShouldSendReminder() {
        entry.setNotificationSent(false);
        entry.setReminderSent(false);
        entry.setJoinedAt(LocalDateTime.now().minusMinutes(20));
        entry.setEstimatedWaitTimeMinutes(22);

        assertTrue(entry.shouldNotify());
        assertTrue(entry.shouldSendReminder());

        entry.setNotificationSent(true);
        entry.setReminderSent(true);
        assertFalse(entry.shouldNotify());
        assertFalse(entry.shouldSendReminder());
    }
}
