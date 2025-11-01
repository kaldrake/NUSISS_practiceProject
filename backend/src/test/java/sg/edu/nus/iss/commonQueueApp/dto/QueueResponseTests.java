/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.QueueResponse;
import sg.edu.nus.iss.commonQueueApp.entity.Queue;
import sg.edu.nus.iss.commonQueueApp.entity.QueueType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueResponseTests {
    @Test
    void testSettersAndGetters() {
        QueueResponse response = new QueueResponse();

        response.setId(1L);
        response.setQueueName("Main Queue");
        response.setDescription("Primary service queue");
        response.setQueueType(QueueType.CONSULTATION);
        response.setAvgServiceTimeMinutes(10);
        response.setCurrentNumber(5);
        response.setNextNumber(6);
        response.setIsActive(true);
        response.setMaxCapacity(50);
        response.setColorCode("#FF0000");
        response.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        response.setUpdatedAt(LocalDateTime.of(2025, 11, 1, 12, 0));

        assertEquals(1L, response.getId());
        assertEquals("Main Queue", response.getQueueName());
        assertEquals("Primary service queue", response.getDescription());
        assertEquals(QueueType.CONSULTATION, response.getQueueType());
        assertEquals(10, response.getAvgServiceTimeMinutes());
        assertEquals(5, response.getCurrentNumber());
        assertEquals(6, response.getNextNumber());
        assertTrue(response.getIsActive());
        assertEquals(50, response.getMaxCapacity());
        assertEquals("#FF0000", response.getColorCode());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 0), response.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 11, 1, 12, 0), response.getUpdatedAt());
    }

    @Test
    void testFromEntity() {
        Queue queue = new Queue();
        queue.setId(1L);
        queue.setQueueName("Main Queue");
        queue.setDescription("Primary service queue");
        queue.setQueueType(QueueType.CASHIER);
        queue.setAvgServiceTimeMinutes(10);
        queue.setCurrentNumber(5);
        queue.setNextNumber(6);
        queue.setIsActive(true);
        queue.setMaxCapacity(50);
        queue.setColorCode("#FF0000");
        queue.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        queue.setUpdatedAt(LocalDateTime.of(2025, 11, 1, 12, 0));

        QueueResponse response = QueueResponse.fromEntity(queue);

        assertEquals(1L, response.getId());
        assertEquals("Main Queue", response.getQueueName());
        assertEquals("Primary service queue", response.getDescription());
        assertEquals(QueueType.CASHIER, response.getQueueType());
        assertEquals(10, response.getAvgServiceTimeMinutes());
        assertEquals(5, response.getCurrentNumber());
        assertEquals(6, response.getNextNumber());
        assertTrue(response.getIsActive());
        assertEquals(50, response.getMaxCapacity());
        assertEquals("#FF0000", response.getColorCode());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 0), response.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 11, 1, 12, 0), response.getUpdatedAt());
    }
}
