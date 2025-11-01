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
public class QueueEntryStatusTests {
    @Test
    void testEnumValues() {
        QueueEntryStatus[] statuses = QueueEntryStatus.values();

        assertEquals(5, statuses.length, "There should be 5 enum constants");

        assertEquals("Waiting", QueueEntryStatus.WAITING.getDisplayName());
        assertEquals("Called", QueueEntryStatus.CALLED.getDisplayName());
        assertEquals("Served", QueueEntryStatus.SERVED.getDisplayName());
        assertEquals("Cancelled", QueueEntryStatus.CANCELLED.getDisplayName());
        assertEquals("No Show", QueueEntryStatus.NO_SHOW.getDisplayName());
    }

    @Test
    void testValueOf() {
        assertEquals(QueueEntryStatus.WAITING, QueueEntryStatus.valueOf("WAITING"));
        assertEquals(QueueEntryStatus.CALLED, QueueEntryStatus.valueOf("CALLED"));
        assertEquals(QueueEntryStatus.SERVED, QueueEntryStatus.valueOf("SERVED"));
        assertEquals(QueueEntryStatus.CANCELLED, QueueEntryStatus.valueOf("CANCELLED"));
        assertEquals(QueueEntryStatus.NO_SHOW, QueueEntryStatus.valueOf("NO_SHOW"));
    }

    @Test
    void testDisplayNameConsistency() {
        for (QueueEntryStatus status : QueueEntryStatus.values()) {
            assertNotNull(status.getDisplayName(), "Display name should not be null for " + status.name());
            assertFalse(status.getDisplayName().isEmpty(), "Display name should not be empty for " + status.name());
        }
    }
}
