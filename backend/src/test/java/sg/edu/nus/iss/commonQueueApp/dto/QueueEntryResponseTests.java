/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.QueueEntryResponse;
import sg.edu.nus.iss.commonQueueApp.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueEntryResponseTests {
    @Test
    void testSettersAndGetters() {
        QueueEntryResponse response = new QueueEntryResponse();

        response.setId(1L);
        response.setQueueId(10L);
        response.setQueueName("Main Queue");
        response.setCustomerId(100L);
        response.setCustomerName("John Doe");
        response.setQueueNumber(5);
        response.setStatus(QueueEntryStatus.WAITING);
        response.setEstimatedWaitTimeMinutes(15);
        response.setJoinedAt(LocalDateTime.now().minusMinutes(20));
        response.setCalledAt(LocalDateTime.now().minusMinutes(10));
        response.setServedAt(LocalDateTime.now());
        response.setPositionInQueue(3);
        response.setBusinessName("Coffee Shop");

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getQueueId());
        assertEquals("Main Queue", response.getQueueName());
        assertEquals(100L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(5, response.getQueueNumber());
        assertEquals(QueueEntryStatus.WAITING, response.getStatus());
        assertEquals(15, response.getEstimatedWaitTimeMinutes());
        assertNotNull(response.getJoinedAt());
        assertNotNull(response.getCalledAt());
        assertNotNull(response.getServedAt());
        assertEquals(3, response.getPositionInQueue());
        assertEquals("Coffee Shop", response.getBusinessName());
    }
    
    @Test
    void fromEntityShouldMapAllFields() {
        // Setup
        Business business = new Business();
        business.setId(1L);
        business.setBusinessName("Coffee Shop");

        Queue queue = new Queue();
        queue.setId(10L);
        queue.setQueueName("Main Queue");
        queue.setBusiness(business);

        QueueEntry entry1 = new QueueEntry(queue, new Customer("John", "91234567"), 1);
        QueueEntry entry2 = new QueueEntry(queue, new Customer("Jane", "91234567"), 2);
        QueueEntry entry3 = new QueueEntry(queue, new Customer("Doe", "91234567"), 3);

        queue.setQueueEntries(List.of(entry1, entry2, entry3));
    
        Customer customer = new Customer();
        customer.setId(100L);
        customer.setName("John Doe");

        QueueEntry entry = entry2;
        entry.setId(1000L);
        entry.setQueue(queue);
        entry.setCustomer(customer);
        entry.setQueueNumber(2);
        entry.setStatus(QueueEntryStatus.WAITING);
        entry.setEstimatedWaitTimeMinutes(15);
        entry.setJoinedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        entry.setCalledAt(LocalDateTime.of(2025, 11, 1, 10, 15));
        entry.setServedAt(LocalDateTime.of(2025, 11, 1, 10, 30));

        // Action
        QueueEntryResponse response = QueueEntryResponse.fromEntity(entry);

        // Assertions
        assertEquals(1000L, response.getId());
        assertEquals(10L, response.getQueueId());
        assertEquals("Main Queue", response.getQueueName());
        assertEquals(100L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(2, response.getQueueNumber());
        assertEquals(QueueEntryStatus.WAITING, response.getStatus());
        assertEquals(15, response.getEstimatedWaitTimeMinutes());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 0), response.getJoinedAt());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 15), response.getCalledAt());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 30), response.getServedAt());
        assertEquals(2, response.getPositionInQueue());
        assertEquals("Coffee Shop", response.getBusinessName());
    }
}
