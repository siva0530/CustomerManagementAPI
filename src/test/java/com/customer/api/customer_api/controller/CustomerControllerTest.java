package com.customer.api.customer_api.controller;

import com.customer.api.customer_api.dto.CustomerRequest;
import com.customer.api.customer_api.dto.CustomerResponse;
import com.customer.api.customer_api.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerResponse sampleResponse;
    private UUID id;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        sampleResponse = CustomerResponse.builder()
                .id(id)
                .name("Alice")
                .email("alice@example.com")
                .annualSpend(new BigDecimal("6000"))
                .lastPurchaseDate(LocalDateTime.of(2024, 9, 1, 0, 0))
                .tier("Gold")
                .build();
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerRequest request = new CustomerRequest("Alice", "alice@example.com", new BigDecimal("6000"),
                LocalDateTime.of(2024, 9, 1, 0, 0));

        Mockito.when(customerService.createCustomer(any())).thenReturn(sampleResponse);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Mockito.when(customerService.getCustomerById(id)).thenReturn(sampleResponse);

        mockMvc.perform(get("/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetCustomerByName() throws Exception {
        Mockito.when(customerService.getCustomerByName("Alice")).thenReturn(sampleResponse);

        mockMvc.perform(get("/customers")
                        .param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetCustomerByEmail() throws Exception {
        Mockito.when(customerService.getCustomerByEmail("alice@example.com")).thenReturn(sampleResponse);

        mockMvc.perform(get("/customers")
                        .param("email", "alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerRequest updateRequest = new CustomerRequest("Alice Updated", "alice.new@example.com",
                new BigDecimal("8000"), LocalDateTime.of(2025, 1, 1, 0, 0));
        Mockito.when(customerService.updateCustomer(eq(id), any())).thenReturn(sampleResponse);

        mockMvc.perform(put("/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Mockito.doNothing().when(customerService).deleteCustomer(id);

        mockMvc.perform(delete("/customers/" + id))
                .andExpect(status().isNoContent());
    }
}