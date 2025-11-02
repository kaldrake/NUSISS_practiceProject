/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;

import jakarta.validation.*;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class BusinessRegistrationRequestTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidRequest_thenNoValidationErrors() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();
        request.setBusinessName("Coffee Haven");
        request.setEmail("info@coffeehaven.com");
        request.setPassword("secret123");
        request.setPhone("12345678");
        request.setAddress("123 Coffee Street");
        request.setDescription("A cozy place for coffee lovers.");
        request.setBusinessType(BusinessType.RESTAURANT);
        request.setOpeningTime(LocalTime.of(8, 0));
        request.setClosingTime(LocalTime.of(22, 0));

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void whenMissingRequiredFields_thenValidationErrors() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();
        // All required fields left null

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation errors for missing fields");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Business name is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    void whenEmailInvalid_thenValidationError() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();
        request.setBusinessName("Test Biz");
        request.setEmail("invalid-email");
        request.setPassword("secret123");

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please provide a valid email")));
    }

    @Test
    void whenPasswordTooShort_thenValidationError() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();
        request.setBusinessName("Test Biz");
        request.setEmail("valid@email.com");
        request.setPassword("123");

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("at least 6 characters")));
    }

    @Test
    void testGettersAndSetters() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();
        request.setBusinessName("Techie Co.");
        request.setEmail("support@techie.com");
        request.setPassword("strongPass123");
        request.setPhone("987654321");
        request.setAddress("1 Tech Lane");
        request.setDescription("Innovative tech solutions.");
        request.setBusinessType(BusinessType.BANK);
        request.setOpeningTime(LocalTime.of(9, 0));
        request.setClosingTime(LocalTime.of(18, 0));

        assertEquals("Techie Co.", request.getBusinessName());
        assertEquals("support@techie.com", request.getEmail());
        assertEquals("strongPass123", request.getPassword());
        assertEquals("987654321", request.getPhone());
        assertEquals("1 Tech Lane", request.getAddress());
        assertEquals("Innovative tech solutions.", request.getDescription());
        assertEquals(BusinessType.BANK, request.getBusinessType());
        assertEquals(LocalTime.of(9, 0), request.getOpeningTime());
        assertEquals(LocalTime.of(18, 0), request.getClosingTime());
    }
}
