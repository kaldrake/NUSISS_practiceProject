/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.QueueStatusResponse;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueStatusResponseTests {
    @Test
    void testSettersAndGetters() {
        QueueStatusResponse response = new QueueStatusResponse();

        response.setQueueId(1L);
        response.setQueueName("VIP Queue");
        response.setCurrentNumber(5);
        response.setNextNumber(6);
        response.setTotalWaiting(10);
        response.setEstimatedWaitTime(15);
        response.setIsActive(true);
        response.setMaxCapacity(50);
        response.setAvgServiceTime(10);
        response.setServedToday(20L);

        assertEquals(1L, response.getQueueId());
        assertEquals("VIP Queue", response.getQueueName());
        assertEquals(5, response.getCurrentNumber());
        assertEquals(6, response.getNextNumber());
        assertEquals(10, response.getTotalWaiting());
        assertEquals(15, response.getEstimatedWaitTime());
        assertTrue(response.getIsActive());
        assertEquals(50, response.getMaxCapacity());
        assertEquals(10, response.getAvgServiceTime());
        assertEquals(20L, response.getServedToday());
    }

    @Test
    void testBuilder() {
        QueueStatusResponse response = QueueStatusResponse.builder()
                .queueId(2L)
                .queueName("Regular Queue")
                .currentNumber(1)
                .nextNumber(2)
                .totalWaiting(5)
                .estimatedWaitTime(12)
                .isActive(false)
                .maxCapacity(100)
                .avgServiceTime(8)
                .servedToday(15L)
                .build();

        assertEquals(2L, response.getQueueId());
        assertEquals("Regular Queue", response.getQueueName());
        assertEquals(1, response.getCurrentNumber());
        assertEquals(2, response.getNextNumber());
        assertEquals(5, response.getTotalWaiting());
        assertEquals(12, response.getEstimatedWaitTime());
        assertFalse(response.getIsActive());
        assertEquals(100, response.getMaxCapacity());
        assertEquals(8, response.getAvgServiceTime());
        assertEquals(15L, response.getServedToday());
    }
}
