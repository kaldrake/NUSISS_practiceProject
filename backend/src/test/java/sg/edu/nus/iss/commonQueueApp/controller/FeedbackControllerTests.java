/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sg.edu.nus.iss.commonQueueApp.dto.FeedbackRequest;
import sg.edu.nus.iss.commonQueueApp.entity.*;
import sg.edu.nus.iss.commonQueueApp.repository.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 *
 * @author junwe
 */
@WebMvcTest(FeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FeedbackControllerTests {
    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private FeedbackRepository feedbackRepository;
    @MockitoBean private CustomerRepository customerRepository;
    @MockitoBean private BusinessRepository businessRepository;
    @MockitoBean private QueueEntryRepository queueEntryRepository;

    private Business business;
    private Customer customer;
    private QueueEntry queueEntry;

    @BeforeEach
    void setUp() {
        business = new Business();
        business.setId(1L);
        business.setBusinessName("Test Business");

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");

        queueEntry = new QueueEntry();
        queueEntry.setId(1L);
        queueEntry.setCustomer(customer);
    }

    // -------------------- submitFeedback --------------------
    @Test
    void submitFeedback_success() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setBusinessId(1L);
        request.setCustomerId(1L);
        request.setQueueEntryId(1L);
        request.setAccuracyRating(5);
        request.setServiceRating(4);
        request.setComment("Great service!");
        request.setIsAnonymous(false);

        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(queueEntryRepository.findById(1L)).thenReturn(Optional.of(queueEntry));
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accuracyRating").value(5))
                .andExpect(jsonPath("$.serviceRating").value(4))
                .andExpect(jsonPath("$.comment").value("Great service!"));

        verify(feedbackRepository).save(any());
    }

    @Test
    void submitFeedback_businessNotFound_throws() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setBusinessId(99L);

        when(businessRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    // -------------------- getBusinessFeedback --------------------
    @Test
    void getBusinessFeedback_success() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setBusiness(business);
        feedback.setAccuracyRating(5);
        feedback.setServiceRating(4);
        feedback.setComment("Good");
        feedback.setCustomer(customer);

        when(feedbackRepository.findByBusinessIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(feedback));

        mockMvc.perform(get("/feedback/business/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accuracyRating").value(5))
                .andExpect(jsonPath("$[0].serviceRating").value(4))
                .andExpect(jsonPath("$[0].comment").value("Good"));
    }

    // -------------------- getCustomerFeedback --------------------
    @Test
    void getCustomerFeedback_success() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setCustomer(customer);
        feedback.setAccuracyRating(4);
        feedback.setServiceRating(5);
        feedback.setComment("Excellent");
        feedback.setBusiness(business);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(feedbackRepository.findByCustomerOrderByCreatedAtDesc(customer)).thenReturn(List.of(feedback));

        mockMvc.perform(get("/feedback/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accuracyRating").value(4))
                .andExpect(jsonPath("$[0].serviceRating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Excellent"));
    }

    @Test
    void getCustomerFeedback_customerNotFound_throws() throws Exception {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/feedback/customer/99"))
                .andExpect(status().isNotFound());
    }
}
