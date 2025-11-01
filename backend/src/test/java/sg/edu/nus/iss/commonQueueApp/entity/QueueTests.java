/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueTests {
    private Business business;
    private Queue queue;

    @BeforeEach
    void setUp() {
        business = new Business("Test Business", "test@example.com", "password");
        queue = new Queue("Test Queue", business);
    }

    @Test
    void testConstructors() {
        // Default constructor
        Queue defaultQueue = new Queue();
        assertNotNull(defaultQueue);

        // Constructor with queueName and business
        Queue queue1 = new Queue("Queue1", business);
        assertEquals("Queue1", queue1.getQueueName());
        assertEquals(business, queue1.getBusiness());

        // Constructor with queueName, business, queueType, avgServiceTimeMinutes
        Queue queue2 = new Queue("Queue2", business, QueueType.PRIORITY, 15);
        assertEquals("Queue2", queue2.getQueueName());
        assertEquals(business, queue2.getBusiness());
        assertEquals(QueueType.PRIORITY, queue2.getQueueType());
        assertEquals(15, queue2.getAvgServiceTimeMinutes());
    }

    @Test
    void testGettersAndSetters() {
        queue.setId(1L);
        queue.setDescription("Sample description");
        queue.setAvgServiceTimeMinutes(12);
        queue.setCurrentNumber(5);
        queue.setNextNumber(10);
        queue.setIsActive(false);
        queue.setQueueType(QueueType.PRIORITY);
        queue.setMaxCapacity(20);
        queue.setColorCode("#FF0000");
        LocalDateTime now = LocalDateTime.now();
        queue.setCreatedAt(now);
        queue.setUpdatedAt(now);
        ArrayList<QueueEntry> entries = new ArrayList<>();
        queue.setQueueEntries(entries);

        assertEquals(1L, queue.getId());
        assertEquals("Sample description", queue.getDescription());
        assertEquals(12, queue.getAvgServiceTimeMinutes());
        assertEquals(5, queue.getCurrentNumber());
        assertEquals(10, queue.getNextNumber());
        assertFalse(queue.getIsActive());
        assertEquals(QueueType.PRIORITY, queue.getQueueType());
        assertEquals(20, queue.getMaxCapacity());
        assertEquals("#FF0000", queue.getColorCode());
        assertEquals(now, queue.getCreatedAt());
        assertEquals(now, queue.getUpdatedAt());
        assertEquals(entries, queue.getQueueEntries());
    }

    @Test
    void testQueueBusinessMethods() {
        // Add mock QueueEntries
        QueueEntry entry1 = new QueueEntry();
        entry1.setStatus(QueueEntryStatus.WAITING);

        QueueEntry entry2 = new QueueEntry();
        entry2.setStatus(QueueEntryStatus.CALLED);

        QueueEntry entry3 = new QueueEntry();
        entry3.setStatus(QueueEntryStatus.SERVED);
        entry3.setServedAt(LocalDateTime.now());

        queue.addQueueEntry(entry1);
        queue.addQueueEntry(entry2);
        queue.addQueueEntry(entry3);

        // Test getCurrentQueueLength (WAITING + CALLED)
        assertEquals(2, queue.getCurrentQueueLength());

        // Test getEstimatedWaitTimeMinutes
        queue.setAvgServiceTimeMinutes(10);
        assertEquals(20, queue.getEstimatedWaitTimeMinutes());

        // Test generateNextNumber
        int nextNumber = queue.getNextNumber();
        Integer generated = queue.generateNextNumber();
        assertEquals(nextNumber, generated);
        assertEquals(nextNumber + 1, queue.getNextNumber());

        // Test isAtCapacity
        queue.setMaxCapacity(3);
        assertFalse(queue.isAtCapacity());
        queue.setMaxCapacity(2);
        assertTrue(queue.isAtCapacity());

        // Test getWaitingCustomersCount
        assertEquals(1, queue.getWaitingCustomersCount());

        // Test getCalledCustomersCount
        assertEquals(1, queue.getCalledCustomersCount());

        // Test getServedTodayCount
        assertEquals(1, queue.getServedTodayCount());

        // Test resetQueue
        queue.resetQueue();
        assertEquals(0, queue.getCurrentNumber());
        assertEquals(1, queue.getNextNumber());

        // Test removeQueueEntry
        queue.removeQueueEntry(entry1);
        assertEquals(2, queue.getQueueEntries().size());
        assertNull(entry1.getQueue());
    }
}
