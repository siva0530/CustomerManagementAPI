package com.customer.api.customer_api.service;

import com.customer.api.customer_api.dto.CustomerRequest;
import com.customer.api.customer_api.dto.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(UUID id);
    CustomerResponse getCustomerByName(String name);
    CustomerResponse getCustomerByEmail(String email);
    CustomerResponse updateCustomer(UUID id, CustomerRequest request);
    void deleteCustomer(UUID id);
}