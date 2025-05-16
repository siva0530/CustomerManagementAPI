# Customer Management API

## Overview

This is a Spring Boot RESTful API that manages customer data with CRUD operations and calculates membership tiers based on annual spending and purchase activity.

## Technologies Used

- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok
- Spring Validation
- Springdoc OpenAPI

## Build and Run

```bash
./gradlew bootRun
```

## API Endpoints

- `POST /customers` – Create a new customer
- `GET /customers/{id}` – Retrieve by ID
- `GET /customers?name={name}` – Retrieve by name
- `GET /customers?email={email}` – Retrieve by email
- `PUT /customers/{id}` – Update customer
- `DELETE /customers/{id}` – Delete customer

## OPEN API

Link to access API Documentation - http://localhost:8080/v3/api-docs

## Assumptions

- Tier is calculated at runtime based on annualSpend and lastPurchaseDate.
- ID is auto-generated (UUID).