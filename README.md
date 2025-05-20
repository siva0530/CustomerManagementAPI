# Customer Management API

## Overview

This is a Spring Boot RESTful API for managing customer data. It provides CRUD operations and calculates membership tiers dynamically based on a customer's annual spending and last purchase date.

## Build and Run

### Build
./gradlew build

### Run
./gradlew bootRun --args='--spring.profiles.active=dev'

Once running, access the application at:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## H2 Database Configuration

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(leave blank)*
- Driver: `org.h2.Driver`

---

## API Endpoints

| Method | Endpoint               | Description              |
|--------|------------------------|--------------------------|
| POST   | `/customers`           | Create a customer        |
| GET    | `/customers/{id}`      | Get customer by ID       |
| GET    | `/customers?name=`     | Get customer by name     |
| GET    | `/customers?email=`    | Get customer by email    |
| PUT    | `/customers/{id}`      | Update customer          |
| DELETE | `/customers/{id}`      | Delete customer          |

---

##  Sample Requests

###  Create Customer
```http
http://localhost:8080/customers

{
  "name": "Alice",
  "email": "alice@example.com",
  "annualSpend": "6000",
  "lastPurchaseDate": "2024-04-01T00:00:00"

}
```
### Sample Response

```http
{
    "id": "c489aca3-c45d-4671-a547-9d0c4273d89e",
    "name": "Alice",
    "email": "alice@example.com",
    "annualSpend": 6000.00,
    "lastPurchaseDate": "2024-09-01T00:00:00",
    "tier": "Gold"
}
```

###  Get Customer by ID
```http
http://localhost:8080/customers/c489aca3-c45d-4671-a547-9d0c4273d89e
GET /customers/{id}
```

### Get Customer by Name
```http
http://localhost:8080/customers?name=Alice
GET /customers?name={name}
```

### Get Customer by Email
```http
http://localhost:8080/customers?email=alice@exmaple.com
GET /customers?name={name}
```

###  Update Customer
```http
PUT /customers/{id}
http://localhost:8080/customers/c489aca3-c45d-4671-a547-9d0c4273d89e

{
    "id": "c489aca3-c45d-4671-a547-9d0c4273d89e",
    "name": "Alice",
    "email": "alice@example.com",
    "annualSpend": 2000,
    "lastPurchaseDate": "2024-09-01T00:00:00",
    "tier": "Gold"
}
```

###  Delete Customer
```http
DELETE /customers/{id}
http://localhost:8080/customers/c489aca3-c45d-4671-a547-9d0c4273d89e
```

---

##  Tier Calculation Logic

- **Silver**: Annual spend < $1000
- **Gold**: Annual spend ≥ $1000 and < $10000 and last purchase within 12 months
- **Platinum**: Annual spend ≥ $10000 and last purchase within 6 months

This value is **calculated at runtime** and appears in the API response — it is not stored in the database.

---

##  Testing

To run unit tests:
```
./gradlew test
```

Includes:
- CRUD flow coverage
- Tier logic unit tests
- Validation and error scenario tests

---

## Assumptions

- The `id` field is auto-generated using UUID.
- The `tier` field is not stored, only computed dynamically in response.