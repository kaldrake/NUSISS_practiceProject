/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.FeedbackType;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class FeedbackTypeTests {
    @Test
    void testEnumValuesAndDisplayNames() {
        assertEquals("Queue Time Accuracy", FeedbackType.QUEUE_ACCURACY.getDisplayName());
        assertEquals("Service Quality", FeedbackType.SERVICE_QUALITY.getDisplayName());
        assertEquals("General Feedback", FeedbackType.GENERAL.getDisplayName());
        assertEquals("Complaint", FeedbackType.COMPLAINT.getDisplayName());
        assertEquals("Suggestion", FeedbackType.SUGGESTION.getDisplayName());

        // Enum values
        FeedbackType[] types = FeedbackType.values();
        assertEquals(5, types.length);
        assertTrue(types.length > 0);
    }
}
