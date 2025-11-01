/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.dto.CustomerResponse;
import sg.edu.nus.iss.commonQueueApp.entity.Customer;
import sg.edu.nus.iss.commonQueueApp.entity.NotificationPreference;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class CustomerResponseTests {
    @Test
    void testSettersAndGetters() {
        CustomerResponse response = new CustomerResponse();

        response.setId(1L);
        response.setName("John Doe");
        response.setEmail("john@example.com");
        response.setPhone("+65 1234 5678");
        response.setNotificationPreference(NotificationPreference.EMAIL);
        response.setLanguagePreference("en");
        response.setIsActive(true);
        response.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        response.setLastLogin(LocalDateTime.of(2025, 11, 1, 12, 0));

        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("+65 1234 5678", response.getPhone());
        assertEquals(NotificationPreference.EMAIL, response.getNotificationPreference());
        assertEquals("en", response.getLanguagePreference());
        assertTrue(response.getIsActive());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 0), response.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 11, 1, 12, 0), response.getLastLogin());
    }
    
    @Test
    void testFromEntityMapsAllFields() {
        // Arrange: create a Customer entity
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setEmail("alice@example.com");
        customer.setPhone("+65 9123 4567");
        customer.setNotificationPreference(NotificationPreference.EMAIL);
        customer.setLanguagePreference("en");
        customer.setIsActive(true);
        customer.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        customer.setLastLogin(LocalDateTime.of(2025, 11, 1, 12, 30));

        // Act: map to CustomerResponse
        CustomerResponse response = CustomerResponse.fromEntity(customer);

        // Assert: all fields are correctly mapped
        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getName(), response.getName());
        assertEquals(customer.getEmail(), response.getEmail());
        assertEquals(customer.getPhone(), response.getPhone());
        assertEquals(customer.getNotificationPreference(), response.getNotificationPreference());
        assertEquals(customer.getLanguagePreference(), response.getLanguagePreference());
        assertEquals(customer.getIsActive(), response.getIsActive());
        assertEquals(customer.getCreatedAt(), response.getCreatedAt());
        assertEquals(customer.getLastLogin(), response.getLastLogin());
    }
}
