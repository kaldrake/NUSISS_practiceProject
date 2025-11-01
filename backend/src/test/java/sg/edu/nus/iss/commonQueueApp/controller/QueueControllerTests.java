/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import sg.edu.nus.iss.commonQueueApp.dto.JoinQueueRequest;
import sg.edu.nus.iss.commonQueueApp.dto.QueueEntryResponse;
import sg.edu.nus.iss.commonQueueApp.dto.QueueResponse;
import sg.edu.nus.iss.commonQueueApp.dto.QueueStatusResponse;
import sg.edu.nus.iss.commonQueueApp.dto.QueueTimingRequest;
import sg.edu.nus.iss.commonQueueApp.entity.Business;
import sg.edu.nus.iss.commonQueueApp.entity.Customer;
import sg.edu.nus.iss.commonQueueApp.entity.Queue;
import sg.edu.nus.iss.commonQueueApp.entity.QueueEntry;
import sg.edu.nus.iss.commonQueueApp.entity.QueueEntryStatus;
import sg.edu.nus.iss.commonQueueApp.entity.QueueType;
import sg.edu.nus.iss.commonQueueApp.service.QueueService;
import sg.edu.nus.iss.commonQueueApp.service.QueueService.QueueStatistics;

/**
 *
 * @author junwe
 */
@WebMvcTest(QueueController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QueueControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueueService queueService;

    @Autowired
    private ObjectMapper objectMapper;

    private Queue queue;
    private QueueEntry queueEntry;
    
    @BeforeEach
    void setUp() {
        queue = new Queue();
        queue.setId(1L);
        queue.setQueueName("Main Queue");
        queue.setCurrentNumber(5);
        queue.setColorCode("#00FF00");
        queue.setAvgServiceTimeMinutes(10);
        queue.setQueueType(QueueType.GENERAL);
        
        Business business = new Business();
        business.setId(1L);
        business.setBusinessName("Test Business");
        queue.setBusiness(business);
        
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");
        customer.setLanguagePreference("EN");

        queueEntry = new QueueEntry();
        queueEntry.setId(100L);
        queueEntry.setQueue(queue);
        queueEntry.setStatus(QueueEntryStatus.WAITING);
        queueEntry.setCustomer(customer);
    }
    
    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testCreateQueue() throws Exception {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setAvgServiceTimeMinutes(10);
        request.setQueueName("Main Queue");

        Mockito.when(queueService.createQueue(anyLong(), any(QueueTimingRequest.class)))
                .thenReturn(queue);

        mockMvc.perform(post("/queues/business/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueName").value("Main Queue"));
    }
    
    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testUpdateQueueTiming() throws Exception {
        QueueTimingRequest request = new QueueTimingRequest();
        request.setAvgServiceTimeMinutes(10);
        request.setQueueName("Main Queue");
        
        Mockito.when(queueService.updateQueueTiming(anyLong(), any(QueueTimingRequest.class)))
                .thenReturn(queue);

        mockMvc.perform(put("/queues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueName").value("Main Queue"));
    }
    
    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testMarkAsServed() throws Exception {
        Mockito.when(queueService.markAsServed(100L)).thenReturn(queueEntry);

        mockMvc.perform(put("/queues/entries/100/served"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }
    
    @Test
    void testGetQueueStatus() throws Exception {
        QueueStatusResponse status = new QueueStatusResponse();
        status.setQueueId(1L);
        status.setCurrentNumber(5);
        status.setTotalWaiting(10);

        Mockito.when(queueService.getQueueStatus(1L)).thenReturn(status);

        mockMvc.perform(get("/queues/1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueId").value(1L))
                .andExpect(jsonPath("$.currentNumber").value(5))
                .andExpect(jsonPath("$.totalWaiting").value(10));
    }

    @Test
    void testJoinQueue() throws Exception {
        JoinQueueRequest request = new JoinQueueRequest();
        request.setCustomerId(99L);

        Mockito.when(queueService.joinQueue(anyLong(), anyLong())).thenReturn(queueEntry);

        mockMvc.perform(post("/queues/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));
    }

    @Test
    @WithMockUser(roles = {"STAFF"})
    void testCallNextCustomer() throws Exception {
        Mockito.when(queueService.callNextCustomer(1L)).thenReturn(queueEntry);

        mockMvc.perform(post("/queues/1/call-next"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));
    }

    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testDeleteQueue() throws Exception {
        mockMvc.perform(delete("/queues/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Queue deleted successfully"));

        Mockito.verify(queueService).deleteQueue(1L);
    }

    @Test
    void testCancelQueueEntry() throws Exception {
        Mockito.when(queueService.cancelQueueEntry(100L))
                .thenReturn(queueEntry);

        mockMvc.perform(put("/queues/entries/100/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }
    
    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testMarkAsNoShow() throws Exception {
        Mockito.when(queueService.markAsNoShow(100L))
                .thenReturn(queueEntry);

        mockMvc.perform(put("/queues/entries/100/no-show"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void testGetCustomerPosition() throws Exception {
        Mockito.when(queueService.getCustomerQueuePosition(1L, 1L))
                .thenReturn(queueEntry);

        mockMvc.perform(get("/queues/1/position/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1));
    }
    
    @Test
    void testGetBusinessQueues() throws Exception {
        Mockito.when(queueService.getActiveQueuesByBusinessId(1L)).thenReturn(List.of(queue));

        mockMvc.perform(get("/queues/business/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].queueName").value("Main Queue"));
    }

    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testGetQueueEntries() throws Exception {
        Mockito.when(queueService.getQueueEntries(1L))
                .thenReturn(List.of(queueEntry));

        mockMvc.perform(get("/queues/1/entries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100));
    }
    
    @Test
    void testGetPublicQueueView() throws Exception {
        Mockito.when(queueService.getActiveQueuesByBusinessId(1L)).thenReturn(List.of(queue));

        mockMvc.perform(get("/queues/public/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].queueId").value(1L))
                .andExpect(jsonPath("$[0].queueName").value("Main Queue"))
                .andExpect(jsonPath("$[0].queueType").value("General Service"));
    }

    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testResetQueue() throws Exception {
        Mockito.doNothing().when(queueService).resetQueue(1L);

        mockMvc.perform(post("/queues/1/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Queue reset successfully"));
    }
    
    @Test
    @WithMockUser(roles = {"BUSINESS"})
    void testGetQueueStatistics() throws Exception {
        QueueStatistics stats = new QueueStatistics();
        stats.setTotalCustomers(20);
        stats.setAverageWaitTimeMinutes(10);

        Mockito.when(queueService.getQueueStatistics(anyLong(), any(), any())).thenReturn(stats);

        mockMvc.perform(get("/queues/business/1/statistics")
                        .param("startDate", LocalDateTime.now().minusDays(1).toString())
                        .param("endDate", LocalDateTime.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(20))
                .andExpect(jsonPath("$.averageWaitTimeMinutes").value(10));
    }
    
    @Test
    void testGetCustomerActiveQueues() throws Exception {
        Mockito.when(queueService.getCustomerQueuePosition(anyLong(), anyLong()))
                .thenReturn(queueEntry);

        mockMvc.perform(get("/queues/customer/50/active"))
                .andExpect(status().isOk());
    }
}
