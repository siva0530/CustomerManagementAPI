package com.customer.api.customer_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class CustomerRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private BigDecimal annualSpend;

    private LocalDateTime lastPurchaseDate;
}