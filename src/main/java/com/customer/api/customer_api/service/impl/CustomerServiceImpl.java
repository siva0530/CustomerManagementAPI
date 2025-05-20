package com.customer.api.customer_api.service.impl;

import com.customer.api.customer_api.dto.CustomerRequest;
import com.customer.api.customer_api.dto.CustomerResponse;
import com.customer.api.customer_api.exception.CustomerNotFoundException;
import com.customer.api.customer_api.model.Customer;
import com.customer.api.customer_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.customer.api.customer_api.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating customer: {}", request);
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .annualSpend(request.getAnnualSpend())
                .lastPurchaseDate(request.getLastPurchaseDate())
                .build();
        customer = customerRepository.save(customer);
        return toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) {
        log.info("Fetching customer by ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByName(String name) {
        log.info("Fetching customer by name: {}", name);
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        log.info("Fetching customer by email: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        log.info("Updating customer ID {}: {}", id, request);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());

        customer = customerRepository.save(customer);
        return toResponse(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        log.info("Deleting customer ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .annualSpend(customer.getAnnualSpend())
                .lastPurchaseDate(customer.getLastPurchaseDate())
                .tier(calculateTier(customer.getAnnualSpend(), customer.getLastPurchaseDate()))
                .build();
    }

    private String calculateTier(BigDecimal spend, LocalDateTime lastPurchase) {
        log.info("Spend = {}, Last Purchase = {}", spend, lastPurchase);

        if (spend == null) return "Silver";

        if (spend.compareTo(new BigDecimal("10000")) >= 0 &&
                lastPurchase != null &&
                lastPurchase.isAfter(LocalDateTime.now().minusMonths(6))) {
            log.info("Returning Tier: Platinum");
            return "Platinum";
        }

        if (spend.compareTo(new BigDecimal("1000")) >= 0 &&
                spend.compareTo(new BigDecimal("10000")) < 0 &&
                lastPurchase != null &&
                lastPurchase.isAfter(LocalDateTime.now().minusMonths(12))) {
            log.info("Returning Tier: Gold");
            return "Gold";
        }

        log.info("Returning Tier: Silver");
        return "Silver";
    }
}