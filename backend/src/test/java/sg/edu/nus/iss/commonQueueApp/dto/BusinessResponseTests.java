/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.dto;

import org.junit.jupiter.api.Test;
import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.entity.BusinessType;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 *
 * @author junwe
 */
public class BusinessResponseTests {
    @Test
    void testSettersAndGetters() {
        BusinessResponse response = new BusinessResponse();

        response.setId(1L);
        response.setBusinessName("Joe's Coffee");
        response.setEmail("joe@coffee.com");
        response.setPhone("+65 1234 5678");
        response.setAddress("123 Coffee Street");
        response.setDescription("Best coffee in town");
        response.setLogoUrl("logo.png");
        response.setThemeColor("#FFFFFF");
        response.setOpeningTime(LocalTime.of(8, 0));
        response.setClosingTime(LocalTime.of(22, 0));
        response.setIsActive(true);
        response.setIsVerified(true);
        response.setBusinessType(BusinessType.RESTAURANT);
        response.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        response.setIsOpen(true);

        assertEquals(1L, response.getId());
        assertEquals("Joe's Coffee", response.getBusinessName());
        assertEquals("joe@coffee.com", response.getEmail());
        assertEquals("+65 1234 5678", response.getPhone());
        assertEquals("123 Coffee Street", response.getAddress());
        assertEquals("Best coffee in town", response.getDescription());
        assertEquals("logo.png", response.getLogoUrl());
        assertEquals("#FFFFFF", response.getThemeColor());
        assertEquals(LocalTime.of(8, 0), response.getOpeningTime());
        assertEquals(LocalTime.of(22, 0), response.getClosingTime());
        assertTrue(response.getIsActive());
        assertTrue(response.getIsVerified());
        assertEquals(BusinessType.RESTAURANT, response.getBusinessType());
        assertEquals(LocalDateTime.of(2025, 11, 1, 10, 0), response.getCreatedAt());
        assertTrue(response.getIsOpen());
    }
    
    @Test
    void testFromEntity() {
        // Arrange
        Business business = new Business();
        business.setId(1L);
        business.setBusinessName("Coffee Shop");
        business.setEmail("coffee@example.com");
        business.setPhone("+65 91234567");
        business.setAddress("123 Main St");
        business.setDescription("Best coffee");
        business.setLogoUrl("logo.png");
        business.setThemeColor("#FFFFFF");
        business.setOpeningTime(LocalTime.of(0, 0));
        business.setClosingTime(LocalTime.of(23, 59));
        business.setIsActive(true);
        business.setIsVerified(true);
        business.setBusinessType(BusinessType.RESTAURANT);
        business.setCreatedAt(LocalDateTime.of(2025, 11, 1, 10, 0));

        // Act
        BusinessResponse response = BusinessResponse.fromEntity(business);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBusinessName()).isEqualTo("Coffee Shop");
        assertThat(response.getEmail()).isEqualTo("coffee@example.com");
        assertThat(response.getPhone()).isEqualTo("+65 91234567");
        assertThat(response.getAddress()).isEqualTo("123 Main St");
        assertThat(response.getDescription()).isEqualTo("Best coffee");
        assertThat(response.getLogoUrl()).isEqualTo("logo.png");
        assertThat(response.getThemeColor()).isEqualTo("#FFFFFF");
        assertThat(response.getOpeningTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(response.getClosingTime()).isEqualTo(LocalTime.of(23, 59));
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getIsVerified()).isTrue();
        assertThat(response.getBusinessType()).isEqualTo(BusinessType.RESTAURANT);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 11, 1, 10, 0));
        assertThat(response.getIsOpen()).isTrue();
    }
}
