/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.repository.BusinessRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
/**
 *
 * @author junwe
 */
public class BusinessUserDetailsServiceTests {
    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessUserDetailsService userDetailsService;

    private Business activeBusiness;
    private Business inactiveBusiness;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        activeBusiness = new Business();
        activeBusiness.setEmail("test@biz.com");
        activeBusiness.setPassword("password123");
        activeBusiness.setIsActive(true);

        inactiveBusiness = new Business();
        inactiveBusiness.setEmail("inactive@biz.com");
        inactiveBusiness.setPassword("secret");
        inactiveBusiness.setIsActive(false);
    }
    
    @Test
    void testLoadUserByUsername_WhenBusinessExistsAndActive() {
        when(businessRepository.findByEmail("test@biz.com")).thenReturn(Optional.of(activeBusiness));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@biz.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@biz.com");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_BUSINESS");
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void testLoadUserByUsername_WhenBusinessIsInactive() {
        when(businessRepository.findByEmail("inactive@biz.com")).thenReturn(Optional.of(inactiveBusiness));

        UserDetails userDetails = userDetailsService.loadUserByUsername("inactive@biz.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.isAccountNonLocked()).isFalse();
        assertThat(userDetails.isEnabled()).isFalse();
    }

    @Test
    void testLoadUserByUsername_WhenBusinessNotFound_ShouldThrowException() {
        when(businessRepository.findByEmail("missing@biz.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("missing@biz.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Business not found with email: missing@biz.com");
    }
}
