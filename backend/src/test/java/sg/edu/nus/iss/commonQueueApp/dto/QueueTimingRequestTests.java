/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.QueueTimingRequest;
import sg.edu.nus.iss.commonQueueApp.entity.QueueType;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueTimingRequestTests {
    @Test
    void testSettersAndGetters() {
        QueueTimingRequest request = new QueueTimingRequest();

        request.setQueueName("VIP Queue");
        request.setDescription("Priority service queue");
        request.setQueueType(QueueType.EXPRESS);
        request.setAvgServiceTimeMinutes(15);
        request.setMaxCapacity(20);
        request.setColorCode("#FF5733");

        assertEquals("VIP Queue", request.getQueueName());
        assertEquals("Priority service queue", request.getDescription());
        assertEquals(QueueType.EXPRESS, request.getQueueType());
        assertEquals(15, request.getAvgServiceTimeMinutes());
        assertEquals(20, request.getMaxCapacity());
        assertEquals("#FF5733", request.getColorCode());
    }

    @Test
    void testBuilder() {
        QueueTimingRequest request = QueueTimingRequest.builder()
                .queueName("Regular Queue")
                .description("Standard service queue")
                .queueType(QueueType.GENERAL)
                .avgServiceTimeMinutes(10)
                .maxCapacity(50)
                .colorCode("#00FF00")
                .build();

        assertEquals("Regular Queue", request.getQueueName());
        assertEquals("Standard service queue", request.getDescription());
        assertEquals(QueueType.GENERAL, request.getQueueType());
        assertEquals(10, request.getAvgServiceTimeMinutes());
        assertEquals(50, request.getMaxCapacity());
        assertEquals("#00FF00", request.getColorCode());
    }
    
    @Test
    void testToString() {
        QueueTimingRequest request = QueueTimingRequest.builder()
                .queueName("VIP Queue")
                .description("Priority queue")
                .queueType(QueueType.EXPRESS)
                .avgServiceTimeMinutes(20)
                .build();

        String toString = request.toString();
        assertTrue(toString.contains("VIP Queue"));
        assertTrue(toString.contains("Priority queue"));
        assertTrue(toString.contains("VIP"));
        assertTrue(toString.contains("20"));
    }

    @Test
    void testOtherConstructor() {
        QueueTimingRequest request = new QueueTimingRequest("Express Queue", QueueType.EXPRESS, 5);

        assertEquals("Express Queue", request.getQueueName());
        assertEquals(QueueType.EXPRESS, request.getQueueType());
        assertEquals(5, request.getAvgServiceTimeMinutes());

        // Other fields should be null
        assertNull(request.getDescription());
        assertNull(request.getMaxCapacity());
        assertNull(request.getColorCode());
    }
}
