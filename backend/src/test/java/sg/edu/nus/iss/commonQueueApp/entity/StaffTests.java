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
    void testStaffRoleMethods() {
        Business dummyBusiness = new Business("Test Business", "test@business.com", "password");

        Staff admin = new Staff("Alice", "alice@example.com", "pass", dummyBusiness, StaffRole.ADMIN);
        Staff manager = new Staff("Bob", "bob@example.com", "pass", dummyBusiness, StaffRole.MANAGER);
        Staff staff = new Staff("Charlie", "charlie@example.com", "pass", dummyBusiness, StaffRole.STAFF);

        // isAdmin
        assertTrue(admin.isAdmin());
        assertFalse(manager.isAdmin());
        assertFalse(staff.isAdmin());

        // isManager
        assertTrue(admin.isManager());      // Admin counts as manager
        assertTrue(manager.isManager());
        assertFalse(staff.isManager());
    }

    @Test
    void testGettersAndSetters() {
        Business dummyBusiness = new Business("Test Business", "test@business.com", "password");

        Staff staff = new Staff();
        staff.setName("John Doe");
        staff.setEmail("john@example.com");
        staff.setPassword("secret");
        staff.setPhone("12345678");
        staff.setRole(StaffRole.MANAGER);
        staff.setBusiness(dummyBusiness);
        staff.setIsActive(true);
        LocalDateTime now = LocalDateTime.now();
        staff.setLastLogin(now);

        assertEquals("John Doe", staff.getName());
        assertEquals("john@example.com", staff.getEmail());
        assertEquals("secret", staff.getPassword());
        assertEquals("12345678", staff.getPhone());
        assertEquals(StaffRole.MANAGER, staff.getRole());
        assertEquals(dummyBusiness, staff.getBusiness());
        assertTrue(staff.getIsActive());
        assertEquals(now, staff.getLastLogin());
    }

    @Test
    void testUpdateLastLogin() throws InterruptedException {
        Business dummyBusiness = new Business("Test Business", "test@business.com", "password");
        Staff staff = new Staff("John Doe", "john@example.com", "pass", dummyBusiness, StaffRole.STAFF);

        assertNull(staff.getLastLogin());
        staff.updateLastLogin();
        assertNotNull(staff.getLastLogin());
        assertTrue(staff.getLastLogin().isBefore(LocalDateTime.now()) || staff.getLastLogin().isEqual(LocalDateTime.now()));
    }
}
