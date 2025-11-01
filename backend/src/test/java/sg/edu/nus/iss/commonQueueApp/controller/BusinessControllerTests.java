/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.repository.BusinessRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author junwe
 */
@WebMvcTest(BusinessController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BusinessControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusinessRepository businessRepository;
    
    // --- Test: Get all active businesses ---
    @Test
    void getAllBusinesses_ReturnsList() throws Exception {
        Business b1 = new Business();
        b1.setId(1L);
        b1.setBusinessName("Alpha");
        b1.setIsActive(true);

        Business b2 = new Business();
        b2.setId(2L);
        b2.setBusinessName("Beta");
        b2.setIsActive(true);

        Mockito.when(businessRepository.findByIsActiveTrueOrderByBusinessNameAsc())
                .thenReturn(List.of(b1, b2));

        mockMvc.perform(get("/businesses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].businessName").value("Alpha"))
                .andExpect(jsonPath("$[1].businessName").value("Beta"));
    }

    // --- Test: Get business by ID (found) ---
    @Test
    void getBusinessById_ReturnsBusiness() throws Exception {
        Business business = new Business();
        business.setId(1L);
        business.setBusinessName("Alpha");
        business.setIsActive(true);

        Mockito.when(businessRepository.findById(1L))
                .thenReturn(Optional.of(business));

        mockMvc.perform(get("/businesses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessName").value("Alpha"));
    }

    // --- Test: Get business by ID (not found) ---
    @Test
    void getBusinessById_NotFound_ReturnsServerError() throws Exception {
        Mockito.when(businessRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/businesses/99"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Business not found"));
    }

    // --- Test: Search businesses by name ---
    @Test
    void searchBusinesses_ReturnsMatches() throws Exception {
        Business b1 = new Business();
        b1.setId(1L);
        b1.setBusinessName("Coffee Shop");
        b1.setIsActive(true);

        Mockito.when(businessRepository
                        .findByBusinessNameContainingIgnoreCaseAndIsActiveTrue(anyString()))
                .thenReturn(List.of(b1));

        mockMvc.perform(get("/businesses/search")
                        .param("q", "coffee")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].businessName").value("Coffee Shop"));
    }
}
