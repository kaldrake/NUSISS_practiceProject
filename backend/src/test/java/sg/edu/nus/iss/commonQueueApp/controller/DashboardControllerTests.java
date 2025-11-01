/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 *
 * @author junwe
 */
@WebMvcTest(DashboardController.class)
public class DashboardControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(username = "business@example.com")
    void testDashboardReturnsExpectedResponse() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful! Welcome to Queue Management System"))
                .andExpect(jsonPath("$.businessEmail").value("business@example.com"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.availableEndpoints").isMap())
                .andExpect(jsonPath("$.availableEndpoints['GET /api/queues/business/{businessId}']")
                        .value("Get your business queues"))
                .andExpect(jsonPath("$.availableEndpoints['POST /api/queues/business/{businessId}']")
                        .value("Create new queue"))
                .andExpect(jsonPath("$.availableEndpoints['GET /api/queues/{queueId}/status']")
                        .value("Get queue status (public)"))
                .andExpect(jsonPath("$.availableEndpoints['POST /api/queues/{queueId}/join']")
                        .value("Customer joins queue (public)"))
                .andExpect(jsonPath("$.availableEndpoints['POST /api/queues/{queueId}/call-next']")
                        .value("Call next customer"))
                .andExpect(jsonPath("$.availableEndpoints['PUT /api/queues/entries/{entryId}/served']")
                        .value("Mark customer as served"));
    }

    @Test
    void testDashboardUnauthorizedWhenNoUser() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}
