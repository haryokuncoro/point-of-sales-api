# POS Spring Boot Application

A minimal **Point of Sales (POS)** backend built with **Spring Boot**, **MySQL**, and **Gradle**.
Includes **JWT authentication**, **unit tests**, and **reporting with PDF and charts**.

---

## Features

* **User Authentication**: Login using JWT tokens.
* **Products & Sales**: CRUD for products and sales transactions.
* **Reporting**: Generate sales reports in PDF with charts.
* **Database**: MySQL integration with Spring Data JPA.
* **Unit Tests**: Example tests using JUnit and Mockito.
* **Gradle Build**: Groovy DSL.

---

## Tech Stack

* Java 17+
* Spring Boot 3.x
* Spring Data JPA
* MySQL
* JWT (JSON Web Tokens)
* PDFBox (PDF reporting)
* Gradle (Groovy DSL)
* JUnit + Mockito

---

## Getting Started

### 1. Clone Repository

```bash
git clone <repository-url>
cd pos-springboot
```

### 2. Configure Database

Create MySQL database:

```sql
CREATE DATABASE pos-api;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pos-api
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Build and Run

```bash
./gradlew bootRun
```

Server will run on: `http://localhost:8080`

---

### 4. Run Unit Tests

```bash
./gradlew test
```

---

### 5. API Endpoints (Example)

* `POST /api/auth/login` - Login and receive JWT token
* `GET /api/products` - List all products
* `POST /api/sales` - Create a sale
* `GET /api/reports/sales/pdf` - Generate PDF sales report

