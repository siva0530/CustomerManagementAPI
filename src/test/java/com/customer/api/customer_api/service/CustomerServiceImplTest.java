package com.customer.api.customer_api.service;

import com.customer.api.customer_api.dto.CustomerRequest;
import com.customer.api.customer_api.dto.CustomerResponse;
import com.customer.api.customer_api.exception.CustomerNotFoundException;
import com.customer.api.customer_api.model.Customer;
import com.customer.api.customer_api.repository.CustomerRepository;
import com.customer.api.customer_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private CustomerRepository repository;
    private CustomerServiceImpl service;
    private UUID mockId;
    private CustomerRequest validRequest;
    private Customer savedCustomer;

    @BeforeEach
    void setup() {
        repository = mock(CustomerRepository.class);
        service = new CustomerServiceImpl(repository);

        mockId = UUID.randomUUID();
        validRequest = new CustomerRequest("John Doe", "john@example.com",
                new BigDecimal("1200"), LocalDateTime.now().minusMonths(3));
        savedCustomer = Customer.builder()
                .id(mockId)
                .name(validRequest.getName())
                .email(validRequest.getEmail())
                .annualSpend(validRequest.getAnnualSpend())
                .lastPurchaseDate(validRequest.getLastPurchaseDate())
                .build();
    }

    @Test
    void testCreateCustomer() {
        when(repository.save(any())).thenReturn(savedCustomer);
        CustomerResponse response = service.createCustomer(validRequest);
        assertEquals("John Doe", response.getName());
        assertEquals("Gold", response.getTier());
    }

    @Test
    void testGetCustomerById() {
        when(repository.findById(mockId)).thenReturn(Optional.of(savedCustomer));
        CustomerResponse response = service.getCustomerById(mockId);
        assertEquals(mockId, response.getId());
    }

    @Test
    void testGetCustomerByName() {
        when(repository.findByName("John Doe")).thenReturn(Optional.of(savedCustomer));
        CustomerResponse response = service.getCustomerByName("John Doe");
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testGetCustomerByEmail() {
        when(repository.findByEmail("john@example.com")).thenReturn(Optional.of(savedCustomer));
        CustomerResponse response = service.getCustomerByEmail("john@example.com");
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void testUpdateCustomer() {
        when(repository.findById(mockId)).thenReturn(Optional.of(savedCustomer));
        when(repository.save(any())).thenReturn(savedCustomer);
        CustomerResponse response = service.updateCustomer(mockId, validRequest);
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testDeleteCustomer() {
        when(repository.findById(mockId)).thenReturn(Optional.of(savedCustomer));
        doNothing().when(repository).delete(savedCustomer);
        assertDoesNotThrow(() -> service.deleteCustomer(mockId));
    }

    @Test
    void testCustomerNotFoundById() {
        when(repository.findById(mockId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(mockId));
    }

    @Test
    void testTierSilver() {
        CustomerRequest request = new CustomerRequest("Low Spend", "low@example.com",
                new BigDecimal("500"), LocalDateTime.now());
        Customer customer = Customer.builder().id(mockId).name("Low Spend").email("low@example.com")
                .annualSpend(request.getAnnualSpend()).lastPurchaseDate(request.getLastPurchaseDate()).build();
        when(repository.save(any())).thenReturn(customer);
        CustomerResponse response = service.createCustomer(request);
        assertEquals("Silver", response.getTier());
    }

    @Test
    void testTierGold() {
        CustomerRequest request = new CustomerRequest("Gold Spend", "gold@example.com",
                new BigDecimal("5000"), LocalDateTime.now().minusMonths(6));
        Customer customer = Customer.builder().id(mockId).name("Gold Spend").email("gold@example.com")
                .annualSpend(request.getAnnualSpend()).lastPurchaseDate(request.getLastPurchaseDate()).build();
        when(repository.save(any())).thenReturn(customer);
        CustomerResponse response = service.createCustomer(request);
        assertEquals("Gold", response.getTier());
    }

    @Test
    void testTierPlatinum() {
        CustomerRequest request = new CustomerRequest("Plat Spend", "plat@example.com",
                new BigDecimal("15000"), LocalDateTime.now().minusMonths(2));
        Customer customer = Customer.builder().id(mockId).name("Plat Spend").email("plat@example.com")
                .annualSpend(request.getAnnualSpend()).lastPurchaseDate(request.getLastPurchaseDate()).build();
        when(repository.save(any())).thenReturn(customer);
        CustomerResponse response = service.createCustomer(request);
        assertEquals("Platinum", response.getTier());
    }
}