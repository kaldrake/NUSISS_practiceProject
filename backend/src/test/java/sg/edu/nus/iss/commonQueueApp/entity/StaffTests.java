/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class StaffTests {
    @Test
    void testGettersAndSetters() {
        Business business = new Business();
        business.setId(1L);
        business.setBusinessName("Demo Business");

        Staff staff = new Staff();

        staff.setId(10L);
        staff.setName("Alice");
        staff.setEmail("alice@example.com");
        staff.setPassword("secret123");
        staff.setPhone("12345678");
        staff.setRole(StaffRole.MANAGER);
        staff.setBusiness(business);
        staff.setIsActive(true);
        LocalDateTime now = LocalDateTime.now();
        staff.setLastLogin(now);
        staff.setCreatedAt(now);
        staff.setUpdatedAt(now);

        assertEquals(10L, staff.getId());
        assertEquals("Alice", staff.getName());
        assertEquals("alice@example.com", staff.getEmail());
        assertEquals("secret123", staff.getPassword());
        assertEquals("12345678", staff.getPhone());
        assertEquals(StaffRole.MANAGER, staff.getRole());
        assertEquals(business, staff.getBusiness());
        assertTrue(staff.getIsActive());
        assertEquals(now, staff.getLastLogin());
        assertEquals(now, staff.getCreatedAt());
        assertEquals(now, staff.getUpdatedAt());
    }
    
    @Test
    void testParameterizedConstructor() {
        Business business = new Business();
        business.setId(2L);
        business.setBusinessName("Test Business");

        Staff staff = new Staff("Bob", "bob@example.com", "password123", business, StaffRole.ADMIN);

        assertEquals("Bob", staff.getName());
        assertEquals("bob@example.com", staff.getEmail());
        assertEquals("password123", staff.getPassword());
        assertEquals(business, staff.getBusiness());
        assertEquals(StaffRole.ADMIN, staff.getRole());

        // Default values
        assertNull(staff.getId());
        assertNull(staff.getPhone());
        assertTrue(staff.getIsActive());
        assertNull(staff.getLastLogin());
        assertNull(staff.getCreatedAt());
        assertNull(staff.getUpdatedAt());
    }

    @Test
    void testIsAdmin() {
        Staff admin = new Staff();
        admin.setRole(StaffRole.ADMIN);

        Staff manager = new Staff();
        manager.setRole(StaffRole.MANAGER);

        Staff staff = new Staff();
        staff.setRole(StaffRole.STAFF);

        assertTrue(admin.isAdmin());
        assertFalse(manager.isAdmin());
        assertFalse(staff.isAdmin());
    }

    @Test
    void testIsManager() {
        Staff admin = new Staff();
        admin.setRole(StaffRole.ADMIN);

        Staff manager = new Staff();
        manager.setRole(StaffRole.MANAGER);

        Staff staff = new Staff();
        staff.setRole(StaffRole.STAFF);

        assertTrue(admin.isManager());   // Admin is also considered manager
        assertTrue(manager.isManager());
        assertFalse(staff.isManager());
    }

    @Test
    void testUpdateLastLogin() {
        Staff staff = new Staff();
        assertNull(staff.getLastLogin());

        staff.updateLastLogin();
        assertNotNull(staff.getLastLogin());

        // Ensure lastLogin is recent (within 2 seconds)
        assertTrue(staff.getLastLogin().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(staff.getLastLogin().isAfter(LocalDateTime.now().minusSeconds(2)));
    }
}
