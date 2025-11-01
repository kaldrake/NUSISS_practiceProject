/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class StaffRoleTests {
    @Test
    void testEnumValues() {
        StaffRole admin = StaffRole.ADMIN;
        StaffRole manager = StaffRole.MANAGER;
        StaffRole staff = StaffRole.STAFF;

        // Check enum names
        assertEquals("ADMIN", admin.name());
        assertEquals("MANAGER", manager.name());
        assertEquals("STAFF", staff.name());

        // Check display names
        assertEquals("Administrator", admin.getDisplayName());
        assertEquals("Manager", manager.getDisplayName());
        assertEquals("Staff Member", staff.getDisplayName());
    }

    @Test
    void testEnumValuesArray() {
        StaffRole[] roles = StaffRole.values();
        assertEquals(3, roles.length);
        assertArrayEquals(new StaffRole[]{StaffRole.ADMIN, StaffRole.MANAGER, StaffRole.STAFF}, roles);
    }

    @Test
    void testEnumValueOf() {
        assertEquals(StaffRole.ADMIN, StaffRole.valueOf("ADMIN"));
        assertEquals(StaffRole.MANAGER, StaffRole.valueOf("MANAGER"));
        assertEquals(StaffRole.STAFF, StaffRole.valueOf("STAFF"));
    }
}
