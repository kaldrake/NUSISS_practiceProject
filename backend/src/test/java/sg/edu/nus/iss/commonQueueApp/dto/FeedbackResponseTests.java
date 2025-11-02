/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.FeedbackType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class FeedbackResponseTests {
    @Test
    void testGettersAndSetters() {
        FeedbackResponse response = new FeedbackResponse();

        response.setId(10L);
        response.setCustomerName("John Doe");
        response.setBusinessName("Test Business");
        response.setAccuracyRating(5);
        response.setServiceRating(4);
        response.setComment("Great service!");
        response.setFeedbackType(FeedbackType.QUEUE_ACCURACY);
        response.setIsAnonymous(false);
        LocalDateTime now = LocalDateTime.now();
        response.setCreatedAt(now);
        response.setOverallRating(4.5);

        assertEquals(10L, response.getId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("Test Business", response.getBusinessName());
        assertEquals(5, response.getAccuracyRating());
        assertEquals(4, response.getServiceRating());
        assertEquals("Great service!", response.getComment());
        assertEquals(FeedbackType.QUEUE_ACCURACY, response.getFeedbackType());
        assertFalse(response.getIsAnonymous());
        assertEquals(now, response.getCreatedAt());
        assertEquals(4.5, response.getOverallRating());
    }

    @Test
    void testFromEntity_anonymous() {
        var feedback = new sg.edu.nus.iss.commonQueueApp.entity.Feedback();
        var customer = new sg.edu.nus.iss.commonQueueApp.entity.Customer();
        customer.setName("John Doe");

        var business = new sg.edu.nus.iss.commonQueueApp.entity.Business();
        business.setBusinessName("Test Business");

        feedback.setId(1L);
        feedback.setCustomer(customer);
        feedback.setBusiness(business);
        feedback.setAccuracyRating(5);
        feedback.setServiceRating(4);
        feedback.setComment("Anonymous feedback");
        feedback.setFeedbackType(FeedbackType.QUEUE_ACCURACY);
        feedback.setIsAnonymous(true);
        feedback.setCreatedAt(LocalDateTime.now());
        

        FeedbackResponse response = FeedbackResponse.fromEntity(feedback);

        assertEquals("Anonymous", response.getCustomerName()); // correctly maps anonymous
        assertEquals("Test Business", response.getBusinessName());
        assertEquals(5, response.getAccuracyRating());
        assertEquals(4, response.getServiceRating());
        assertEquals("Anonymous feedback", response.getComment());
        assertTrue(response.getIsAnonymous());
        assertEquals(FeedbackType.QUEUE_ACCURACY, response.getFeedbackType());
        assertEquals(4.5, response.getOverallRating());
    }

    @Test
    void testFromEntity_nonAnonymous() {
        var feedback = new sg.edu.nus.iss.commonQueueApp.entity.Feedback();
        var customer = new sg.edu.nus.iss.commonQueueApp.entity.Customer();
        customer.setName("Alice");

        var business = new sg.edu.nus.iss.commonQueueApp.entity.Business();
        business.setBusinessName("Demo Business");

        feedback.setCustomer(customer);
        feedback.setBusiness(business);
        feedback.setAccuracyRating(3);
        feedback.setServiceRating(5);
        feedback.setComment("Good service");
        feedback.setIsAnonymous(false);
        feedback.setFeedbackType(FeedbackType.QUEUE_ACCURACY);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setAccuracyRating(4);
        feedback.setServiceRating(4);

        FeedbackResponse response = FeedbackResponse.fromEntity(feedback);

        assertEquals("Alice", response.getCustomerName());
        assertEquals("Demo Business", response.getBusinessName());
        assertEquals(4, response.getAccuracyRating());
        assertEquals(4, response.getServiceRating());
        assertEquals("Good service", response.getComment());
        assertFalse(response.getIsAnonymous());
        assertEquals(FeedbackType.QUEUE_ACCURACY, response.getFeedbackType());
        assertEquals(4, response.getOverallRating());
    }
}
