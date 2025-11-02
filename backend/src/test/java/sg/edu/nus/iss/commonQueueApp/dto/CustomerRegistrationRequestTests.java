/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class CustomerRegistrationRequestTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidRequest_thenNoValidationErrors() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Alice Tan");
        request.setEmail("alice@example.com");
        request.setPhone("91234567");
        request.setLanguagePreference("en");

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void whenNameMissing_thenValidationError() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setEmail("user@example.com");

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation error for missing name");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name is required")));
    }

    @Test
    void whenEmailInvalid_thenValidationError() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("not-an-email");

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation error for invalid email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please provide a valid email")));
    }

    @Test
    void whenPhoneTooLong_thenValidationError() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Jane Doe");
        request.setPhone("123456789012345678901"); // 21 chars

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation error for phone length");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number must not exceed 20 characters")));
    }

    @Test
    void whenLanguagePreferenceNotSet_thenDefaultIsEnglish() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        assertEquals("en", request.getLanguagePreference(), "Default language should be 'en'");
    }

    @Test
    void testGettersAndSetters() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Tom Lee");
        request.setEmail("tom@example.com");
        request.setPhone("98765432");
        request.setLanguagePreference("zh");

        assertEquals("Tom Lee", request.getName());
        assertEquals("tom@example.com", request.getEmail());
        assertEquals("98765432", request.getPhone());
        assertEquals("zh", request.getLanguagePreference());
    }
}
