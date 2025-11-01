/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class CustomerRegistrationRequestTests {
    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoConstraintViolations() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .name("Alice")
                .email("alice@example.com")
                .phone("+65 9123 4567")
                .languagePreference("en")
                .build();

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "There should be no constraint violations");
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .name("   ") // blank name
                .email("alice@example.com")
                .build();

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Name should not be blank");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void whenEmailIsInvalid_thenValidationFails() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .name("Alice")
                .email("invalid-email") // invalid email
                .build();

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Email should be invalid");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void whenPhoneTooLong_thenValidationFails() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .name("Alice")
                .phone("1234567890123456789012345") // more than 20 chars
                .build();

        Set<ConstraintViolation<CustomerRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Phone number should exceed max length");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
    }

    @Test
    void whenLanguagePreferenceNotSet_thenDefaultsToEn() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .name("Alice")
                .email("alice@example.com")
                .build();

        assertEquals("en", request.getLanguagePreference(), "Default language should be 'en'");
    }
}
