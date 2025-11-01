/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.*;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class BusinessTests {
    @Test
    void testSettersAndGetters() {
        Business business = new Business();

        business.setId(1L);
        business.setBusinessName("Test Business");
        business.setEmail("test@example.com");
        business.setPassword("secret");
        business.setPhone("+65 1234 5678");
        business.setAddress("123 Main St");
        business.setDescription("Description");
        business.setLogoUrl("logo.png");
        business.setThemeColor("#FF0000");
        business.setOpeningTime(LocalTime.of(8, 0));
        business.setClosingTime(LocalTime.of(20, 0));
        business.setIsActive(true);
        business.setIsVerified(true);
        business.setBusinessType(BusinessType.RESTAURANT);
        LocalDateTime now = LocalDateTime.now();
        business.setCreatedAt(now);
        business.setUpdatedAt(now);

        assertEquals(1L, business.getId());
        assertEquals("Test Business", business.getBusinessName());
        assertEquals("test@example.com", business.getEmail());
        assertEquals("secret", business.getPassword());
        assertEquals("+65 1234 5678", business.getPhone());
        assertEquals("123 Main St", business.getAddress());
        assertEquals("Description", business.getDescription());
        assertEquals("logo.png", business.getLogoUrl());
        assertEquals("#FF0000", business.getThemeColor());
        assertEquals(LocalTime.of(8, 0), business.getOpeningTime());
        assertEquals(LocalTime.of(20, 0), business.getClosingTime());
        assertTrue(business.getIsActive());
        assertTrue(business.getIsVerified());
        assertEquals(BusinessType.RESTAURANT, business.getBusinessType());
        assertEquals(now, business.getCreatedAt());
        assertEquals(now, business.getUpdatedAt());
    }

    @Test
    void testConstructorWithParams() {
        Business business = new Business("My Business", "email@domain.com", "password123");

        assertEquals("My Business", business.getBusinessName());
        assertEquals("email@domain.com", business.getEmail());
        assertEquals("password123", business.getPassword());

        // Other fields should have default values
        assertNull(business.getPhone());
        assertNull(business.getAddress());
        assertNull(business.getDescription());
        assertNull(business.getLogoUrl());
        assertEquals("#007bff", business.getThemeColor());
        assertNull(business.getOpeningTime());
        assertNull(business.getClosingTime());
        assertTrue(business.getIsActive());
        assertFalse(business.getIsVerified());
    }

    @Test
    void testIsOpen() {
        Business business = new Business();
        business.setIsActive(true);
        business.setOpeningTime(LocalTime.now().minusHours(1));
        business.setClosingTime(LocalTime.now().plusHours(1));
        assertTrue(business.isOpen());

        business.setIsActive(false);
        assertFalse(business.isOpen());

        business.setIsActive(true);
        business.setOpeningTime(LocalTime.now().plusHours(1));
        business.setClosingTime(LocalTime.now().plusHours(2));
        assertFalse(business.isOpen());

        business.setOpeningTime(null);
        business.setClosingTime(null);
        assertFalse(business.isOpen());
    }

    @Test
    void testAddAndRemoveQueue() {
        Business business = new Business();
        Queue queue = new Queue();
        queue.setBusiness(null);

        business.addQueue(queue);
        assertTrue(business.getQueues().contains(queue));
        assertEquals(business, queue.getBusiness());

        business.removeQueue(queue);
        assertFalse(business.getQueues().contains(queue));
        assertNull(queue.getBusiness());
    }

    @Test
    void testQueuesAndStaffInitialization() {
        Business business = new Business();
        assertNotNull(business.getQueues());
        assertNotNull(business.getStaff());

        business.setQueues(Collections.emptyList());
        business.setStaff(Collections.emptyList());
        assertEquals(0, business.getQueues().size());
        assertEquals(0, business.getStaff().size());
    }
}
