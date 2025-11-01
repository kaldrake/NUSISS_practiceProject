/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.entity;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author junwe
 */
public class FeedbackTests {
    @Test
    void testConstructorsAndSettersGetters() {
        Customer customer = new Customer("John Doe", "john@mail.com", "+6512345678");
        Business business = new Business("Biz1", "b@mail.com", "pass");
        QueueEntry queueEntry = new QueueEntry();

        // Default constructor + setters
        Feedback feedback1 = new Feedback();
        feedback1.setCustomer(customer);
        feedback1.setBusiness(business);
        feedback1.setQueueEntry(queueEntry);
        feedback1.setAccuracyRating(5);
        feedback1.setServiceRating(4);
        feedback1.setComment("Great service!");
        feedback1.setFeedbackType(FeedbackType.SERVICE_QUALITY);
        feedback1.setIsAnonymous(true);

        LocalDateTime now = LocalDateTime.now();
        feedback1.setCreatedAt(now);
        feedback1.setUpdatedAt(now);

        assertEquals(customer, feedback1.getCustomer());
        assertEquals(business, feedback1.getBusiness());
        assertEquals(queueEntry, feedback1.getQueueEntry());
        assertEquals(5, feedback1.getAccuracyRating());
        assertEquals(4, feedback1.getServiceRating());
        assertEquals("Great service!", feedback1.getComment());
        assertEquals(FeedbackType.SERVICE_QUALITY, feedback1.getFeedbackType());
        assertTrue(feedback1.getIsAnonymous());
        assertEquals(now, feedback1.getCreatedAt());
        assertEquals(now, feedback1.getUpdatedAt());

        // Constructor with customer, business, accuracyRating
        Feedback feedback2 = new Feedback(customer, business, 3);
        assertEquals(customer, feedback2.getCustomer());
        assertEquals(business, feedback2.getBusiness());
        assertEquals(3, feedback2.getAccuracyRating());

        // Constructor with customer, business, queueEntry, accuracyRating, comment
        Feedback feedback3 = new Feedback(customer, business, queueEntry, 4, "Good queue");
        assertEquals(customer, feedback3.getCustomer());
        assertEquals(business, feedback3.getBusiness());
        assertEquals(queueEntry, feedback3.getQueueEntry());
        assertEquals(4, feedback3.getAccuracyRating());
        assertEquals("Good queue", feedback3.getComment());
    }

    @Test
    void testIsPositiveFeedback() {
        Feedback feedback = new Feedback();
        feedback.setAccuracyRating(5);
        assertTrue(feedback.isPositiveFeedback());

        feedback.setAccuracyRating(4);
        assertTrue(feedback.isPositiveFeedback());

        feedback.setAccuracyRating(3);
        assertFalse(feedback.isPositiveFeedback());

        feedback.setAccuracyRating(null);
        assertFalse(feedback.isPositiveFeedback());
    }

    @Test
    void testGetOverallRating() {
        Feedback feedback = new Feedback();
        feedback.setAccuracyRating(4);
        assertEquals(4.0, feedback.getOverallRating());

        feedback.setServiceRating(2);
        assertEquals(3.0, feedback.getOverallRating());

        feedback.setAccuracyRating(null);
        assertNull(feedback.getOverallRating());
    }
}
