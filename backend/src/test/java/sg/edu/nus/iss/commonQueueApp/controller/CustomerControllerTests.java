/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.controller;

import sg.edu.nus.iss.commonQueueApp.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import sg.edu.nus.iss.commonQueueApp.dto.CustomerRegistrationRequest;
import sg.edu.nus.iss.commonQueueApp.entity.Customer;
/**
 *
 * @author junwe
 */
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");
        customer.setLanguagePreference("EN");
    }
    
    @Test
    void testRegisterCustomer() throws Exception {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPhone("1234567890");
        request.setLanguagePreference("EN");

        Mockito.when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()))
                .andExpect(jsonPath("$.languagePreference").value(customer.getLanguagePreference()));
    }

    @Test
    void testGetCustomer() throws Exception {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()))
                .andExpect(jsonPath("$.languagePreference").value(customer.getLanguagePreference()));
    }

    @Test
    void testGetCustomerNotFound() throws Exception {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Customer not found"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPhone("9876543210");
        request.setLanguagePreference("FR");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName(request.getName());
        updatedCustomer.setEmail(request.getEmail());
        updatedCustomer.setPhone(request.getPhone());
        updatedCustomer.setLanguagePreference(request.getLanguagePreference());

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCustomer.getId()))
                .andExpect(jsonPath("$.name").value(updatedCustomer.getName()))
                .andExpect(jsonPath("$.email").value(updatedCustomer.getEmail()))
                .andExpect(jsonPath("$.phone").value(updatedCustomer.getPhone()))
                .andExpect(jsonPath("$.languagePreference").value(updatedCustomer.getLanguagePreference()));
    }
}
