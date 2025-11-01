/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 *
 * @author junwe
 */
public class BusinessRegistrationRequestTests {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBuilderAndGetters() {
        BusinessRegistrationRequest request = BusinessRegistrationRequest.builder()
                .businessName("Joe's Coffee Shop")
                .email("joe@example.com")
                .password("securePass")
                .phone("+65 98765432")
                .address("123 Main Street")
                .description("Best coffee")
                .businessType(BusinessType.RESTAURANT)
                .openingTime(LocalTime.of(8, 0))
                .closingTime(LocalTime.of(22, 0))
                .build();

        assertThat(request.getBusinessName()).isEqualTo("Joe's Coffee Shop");
        assertThat(request.getEmail()).isEqualTo("joe@example.com");
        assertThat(request.getPassword()).isEqualTo("securePass");
        assertThat(request.getPhone()).isEqualTo("+65 98765432");
        assertThat(request.getAddress()).isEqualTo("123 Main Street");
        assertThat(request.getDescription()).isEqualTo("Best coffee");
        assertThat(request.getBusinessType()).isEqualTo(BusinessType.RESTAURANT);
        assertThat(request.getOpeningTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(request.getClosingTime()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    void testValidationFailsForBlankBusinessName() {
        BusinessRegistrationRequest request = BusinessRegistrationRequest.builder()
                .businessName("")
                .email("joe@example.com")
                .password("securePass")
                .build();

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("businessName")
                && v.getMessage().contains("required"));
    }

    @Test
    void testValidationFailsForInvalidEmail() {
        BusinessRegistrationRequest request = BusinessRegistrationRequest.builder()
                .businessName("Coffee Shop")
                .email("invalid-email")
                .password("securePass")
                .build();

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().contains("valid email"));
    }

    @Test
    void testValidationFailsForShortPassword() {
        BusinessRegistrationRequest request = BusinessRegistrationRequest.builder()
                .businessName("Coffee Shop")
                .email("joe@example.com")
                .password("123")
                .build();

        Set<ConstraintViolation<BusinessRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password")
                && v.getMessage().contains("at least 6 characters"));
    }

    @Test
    void testSetters() {
        BusinessRegistrationRequest request = new BusinessRegistrationRequest();

        request.setBusinessName("Joe's Coffee");
        request.setEmail("joe@coffee.com");
        request.setPassword("secret123");
        request.setPhone("+65 1234 5678");
        request.setAddress("123 Coffee Street");
        request.setDescription("Best coffee in town");
        request.setBusinessType(BusinessType.RESTAURANT);
        request.setOpeningTime(LocalTime.of(8, 0));
        request.setClosingTime(LocalTime.of(22, 0));

        assertEquals("Joe's Coffee", request.getBusinessName());
        assertEquals("joe@coffee.com", request.getEmail());
        assertEquals("secret123", request.getPassword());
        assertEquals("+65 1234 5678", request.getPhone());
        assertEquals("123 Coffee Street", request.getAddress());
        assertEquals("Best coffee in town", request.getDescription());
        assertEquals(BusinessType.RESTAURANT, request.getBusinessType());
        assertEquals(LocalTime.of(8, 0), request.getOpeningTime());
        assertEquals(LocalTime.of(22, 0), request.getClosingTime());
    }
}
