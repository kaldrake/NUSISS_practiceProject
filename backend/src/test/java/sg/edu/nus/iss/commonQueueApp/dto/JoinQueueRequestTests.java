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
public class JoinQueueRequestTests {
    @Test
    void testGettersAndSetters() {
        JoinQueueRequest request = new JoinQueueRequest();
        Long expectedCustomerId = 123L;

        request.setCustomerId(expectedCustomerId);

        assertEquals(expectedCustomerId, request.getCustomerId(),
                "Getter should return the same customerId set by the setter");
    }

    @Test
    void testDefaultConstructor() {
        JoinQueueRequest request = new JoinQueueRequest();
        assertNull(request.getCustomerId(), "Default customerId should be null");
    }

    @Test
    void testUpdateCustomerId() {
        JoinQueueRequest request = new JoinQueueRequest();
        request.setCustomerId(1L);
        assertEquals(1L, request.getCustomerId());

        request.setCustomerId(2L);
        assertEquals(2L, request.getCustomerId(), "CustomerId should update correctly");
    }
}
