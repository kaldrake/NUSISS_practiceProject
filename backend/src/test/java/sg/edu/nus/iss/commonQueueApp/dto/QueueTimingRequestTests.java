/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.QueueType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class QueueTimingRequestTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidRequest_thenNoValidationErrors() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("Customer Support Queue");
        request.setDescription("Handles all customer support tickets.");
        request.setQueueType(QueueType.CONSULTATION);
        request.setAvgServiceTimeMinutes(10);
        request.setMaxCapacity(50);
        request.setColorCode("#FF9900");

        Set<ConstraintViolation<QueueTimingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors for valid request");
    }

    @Test
    void whenQueueNameMissing_thenValidationError() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setAvgServiceTimeMinutes(5);

        Set<ConstraintViolation<QueueTimingRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation error for missing queue name");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Queue name is required")));
    }

    @Test
    void whenQueueNameTooLong_thenValidationError() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("A".repeat(101)); // exceeds 100 chars
        request.setAvgServiceTimeMinutes(5);

        Set<ConstraintViolation<QueueTimingRequest>> violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must not exceed 100 characters")));
    }

    @Test
    void whenDescriptionTooLong_thenValidationError() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("Support Queue");
        request.setDescription("B".repeat(256)); // exceeds 255 chars
        request.setAvgServiceTimeMinutes(5);

        Set<ConstraintViolation<QueueTimingRequest>> violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Description must not exceed 255 characters")));
    }

    @Test
    void whenAvgServiceTimeNonPositive_thenValidationError() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("Fast Queue");
        request.setAvgServiceTimeMinutes(0); // invalid

        Set<ConstraintViolation<QueueTimingRequest>> violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be positive")));
    }

    @Test
    void testParameterizedConstructor() {
        QueueTimingRequest request = new QueueTimingRequest(
                "VIP Queue",
                QueueType.PRIORITY,
                8
        );

        assertEquals("VIP Queue", request.getQueueName());
        assertEquals(QueueType.PRIORITY, request.getQueueType());
        assertEquals(8, request.getAvgServiceTimeMinutes());
    }

    @Test
    void testGettersAndSetters() {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setQueueName("Regular Queue");
        request.setDescription("Handles walk-in customers");
        request.setQueueType(QueueType.GENERAL);
        request.setAvgServiceTimeMinutes(12);
        request.setMaxCapacity(100);
        request.setColorCode("#00FF00");

        assertEquals("Regular Queue", request.getQueueName());
        assertEquals("Handles walk-in customers", request.getDescription());
        assertEquals(QueueType.GENERAL, request.getQueueType());
        assertEquals(12, request.getAvgServiceTimeMinutes());
        assertEquals(100, request.getMaxCapacity());
        assertEquals("#00FF00", request.getColorCode());
    }

    @Test
    void testDefaultConstructorInitialValues() {
        QueueTimingRequest request = new QueueTimingRequest();

        assertNull(request.getQueueName());
        assertNull(request.getDescription());
        assertNull(request.getQueueType());
        assertNull(request.getAvgServiceTimeMinutes());
        assertNull(request.getMaxCapacity());
        assertNull(request.getColorCode());
    }
}
