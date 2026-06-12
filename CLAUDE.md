# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Hospital Management System REST API built with Spring Boot 3.3.5 on Java 17. Manages patients, doctors, appointments, billing, and insurance. Uses an in-memory H2 database that is recreated on every startup, so there is no persistence between runs.

## Commands

The project uses the Maven wrapper (`mvnw` / `mvnw.cmd`). On Windows use `mvnw.cmd`.

```bash
./mvnw spring-boot:run          # Run the app on http://localhost:8080
./mvnw clean package            # Build the executable jar into target/
./mvnw test                     # Run all tests
./mvnw test -Dtest=HospitalManagementApplicationTests   # Run a single test class
./mvnw test -Dtest=SomeClass#someMethod                 # Run a single test method
```

Key URLs when running:
- Swagger UI / API docs: `http://localhost:8080/swagger-ui.html` (springdoc-openapi)
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:hospitaldb`, user `sa`, no password)

## Architecture

Classic layered Spring Boot architecture. Each domain (Patient, Doctor, Appointment, Bill, Insurance) has a parallel set of classes across these layers:

- `controller/` — `@RestController` classes under `/api/{domain}` returning entities directly (no response-DTO mapping; entities are serialized as-is).
- `service/` + `service/impl/` — **interface-and-implementation pattern**. Define the contract in `service/XxxService.java`, implement in `service/impl/XxxServiceImpl.java`. Impls are `@Service @Transactional @RequiredArgsConstructor`, with read methods marked `@Transactional(readOnly = true)`. Business rules and cross-entity validation live here, not in controllers.
- `repository/` — Spring Data JPA `JpaRepository` interfaces. Derived query methods (e.g. `findByDoctorIdAndAppointmentDateTime`, `existsBy...`) are used for lookups and uniqueness checks.
- `dto/` — request DTOs with Jakarta Bean Validation annotations; `@Valid` is applied at the controller boundary. DTOs carry foreign keys as IDs (e.g. `patientId`, `doctorId`), which services resolve to entities.
- `entity/` — JPA entities. Enums (`AppointmentStatus`, `PaymentStatus`, `InsuranceStatus`) live alongside them.

### Conventions that span files

- **`BaseEntity`** (`@MappedSuperclass`) provides `id` (IDENTITY-generated), `createdAt`, and `updatedAt`, auto-populated via `@PrePersist`/`@PreUpdate`. New entities should extend it rather than redeclaring these fields.
- **Lombok** is used throughout — `@RequiredArgsConstructor` for constructor injection (no `@Autowired`), plus `@Getter`/`@Setter`/`@Data`. Dependencies are `private final` fields.
- **Exception handling is centralized** in `exception/GlobalExceptionHandler` (`@RestControllerAdvice`). Throw the domain exceptions instead of building error responses in controllers:
  - `ResourceNotFoundException` → 404
  - `DuplicateResourceException` → 409
  - `BusinessException` → 400 (use for business-rule violations, e.g. inactive patient, double-booked doctor)
  - Bean-validation failures → 400 with per-field messages
  - All responses use the `ApiError` shape (timestamp, status, error, message, path, fieldErrors).
- **Seed data**: `config/DataInitializer` (a `CommandLineRunner`) populates patients, doctors, insurances, appointments, and bills on startup — but only if the patient table is empty. Because the DB is `create-drop`, this runs on every launch.

## Code authoring standards

Every file you create or modify in this repository MUST follow these rules. Use the comment syntax of the file's language (Javadoc `/** */` for Java).

1. **Author** — The top-of-file header comment must name the author as **CitiDeveloper**.
2. **Version** — The header must declare the file version as **V1**.
3. **Documentation** — Every class, interface, and method must have a doc comment describing its purpose, parameters, and return value.
4. **File summary** — A summary comment block at the **bottom of the file** must recap what the file/class does and list the methods it contains.

Example header and summary for a Java file:

```java
/**
 * Author: CitiDeveloper
 * Version: V1
 *
 * OrderService — handles order creation and lifecycle.
 */
public class OrderService {

    /**
     * Places a new order for the given customer.
     * @param customerId the customer placing the order
     * @return the generated order id
     */
    public String placeOrder(String customerId) { ... }
}

/*
 * ---- Summary ----
 * OrderService: creates and manages customer orders.
 * Methods: placeOrder(String)
 */
```

## Notes

- The `out/` directory holds a PlantUML class diagram (`src/main/resources/class-diagram.wsd`) and rendered PNGs — generated artifacts, not source.
