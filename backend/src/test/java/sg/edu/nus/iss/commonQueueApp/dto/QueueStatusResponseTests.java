/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueStatusResponseTests {
    @Test
    void testGettersAndSetters() {
        QueueStatusResponse response = new QueueStatusResponse();

        Long queueId = 1L;
        String queueName = "Main Queue";
        Integer currentNumber = 10;
        Integer nextNumber = 11;
        Integer totalWaiting = 5;
        Integer estimatedWaitTime = 15;
        Boolean isActive = true;
        Integer maxCapacity = 100;
        Integer avgServiceTime = 3;
        Long servedToday = 250L;

        response.setQueueId(queueId);
        response.setQueueName(queueName);
        response.setCurrentNumber(currentNumber);
        response.setNextNumber(nextNumber);
        response.setTotalWaiting(totalWaiting);
        response.setEstimatedWaitTime(estimatedWaitTime);
        response.setIsActive(isActive);
        response.setMaxCapacity(maxCapacity);
        response.setAvgServiceTime(avgServiceTime);
        response.setServedToday(servedToday);

        assertEquals(queueId, response.getQueueId());
        assertEquals(queueName, response.getQueueName());
        assertEquals(currentNumber, response.getCurrentNumber());
        assertEquals(nextNumber, response.getNextNumber());
        assertEquals(totalWaiting, response.getTotalWaiting());
        assertEquals(estimatedWaitTime, response.getEstimatedWaitTime());
        assertEquals(isActive, response.getIsActive());
        assertEquals(maxCapacity, response.getMaxCapacity());
        assertEquals(avgServiceTime, response.getAvgServiceTime());
        assertEquals(servedToday, response.getServedToday());
    }

    @Test
    void testDefaultConstructorAndInitialValues() {
        QueueStatusResponse response = new QueueStatusResponse();

        assertNull(response.getQueueId());
        assertNull(response.getQueueName());
        assertNull(response.getCurrentNumber());
        assertNull(response.getNextNumber());
        assertNull(response.getTotalWaiting());
        assertNull(response.getEstimatedWaitTime());
        assertNull(response.getIsActive());
        assertNull(response.getMaxCapacity());
        assertNull(response.getAvgServiceTime());
        assertNull(response.getServedToday());
    }

    @Test
    void testUpdateFields() {
        QueueStatusResponse response = new QueueStatusResponse();

        response.setQueueName("Initial Queue");
        assertEquals("Initial Queue", response.getQueueName());

        response.setQueueName("Updated Queue");
        assertEquals("Updated Queue", response.getQueueName(), "Queue name should update correctly");
    }
}
