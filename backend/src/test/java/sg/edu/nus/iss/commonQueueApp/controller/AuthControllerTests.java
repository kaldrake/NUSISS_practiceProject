/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.repository.BusinessRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 *
 * @author junwe
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private BusinessRepository businessRepository;
    
    @Test
    void businessLogin_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);

        Business business = new Business();
        business.setEmail(email);
        Mockito.when(businessRepository.findByEmail(eq(email))).thenReturn(Optional.of(business));

        // Act & Assert
        mockMvc.perform(post("/auth/business/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.business.email").value(email))
                .andExpect(jsonPath("$.token").exists());
    }
    
    @Test
    void businessLogin_InvalidCredentials() throws Exception {
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail("bad@example.com");
        loginRequest.setPassword("wrong");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/business/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void businessLogin_BusinessNotFound() throws Exception {
        String email = "notfound@example.com";
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password");

        Authentication auth = new UsernamePasswordAuthenticationToken(email, "password");
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);
        Mockito.when(businessRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/business/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void businessLogout_Success() throws Exception {
        mockMvc.perform(post("/auth/business/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    void getAuthStatus_AuthenticatedUser() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken("john@example.com", "password", "ROLE_USER")
        );
        
        mockMvc.perform(get("/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.username").value("john@example.com"));
        
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAuthStatus_NotAuthenticated() throws Exception {
        mockMvc.perform(get("/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false))
                .andExpect(jsonPath("$.username").doesNotExist());
    }
}
