/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.JoinQueueRequest;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class JoinQueueRequestTests {
    @Test
    void builderShouldSetCustomerId() {
        JoinQueueRequest request = JoinQueueRequest.builder()
                .customerId(123L)
                .build();

        assertEquals(123L, request.getCustomerId());
    }

    @Test
    void setterShouldUpdateCustomerId() {
        JoinQueueRequest request = new JoinQueueRequest();
        request.setCustomerId(456L);

        assertEquals(456L, request.getCustomerId());
    }

    @Test
    void toStringShouldContainCustomerId() {
        JoinQueueRequest request = JoinQueueRequest.builder()
                .customerId(789L)
                .build();

        String str = request.toString();
        assertTrue(str.contains("789"));
    }
}
