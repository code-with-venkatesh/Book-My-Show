# 🎬 BookMyShow Backend API

A production-oriented Spring Boot backend application inspired by BookMyShow. The platform allows users to browse movies, manage theaters, book tickets, select seats, and receive booking notifications.

This project has been modernized and enhanced by **Venkatesh I** with a focus on clean architecture, scalability, security, and backend engineering best practices.

---

## 🚀 Technology Stack

| Technology | Version |
|------------|----------|
| Java | 21 (LTS) |
| Spring Boot | 3.x |
| Spring MVC | Latest |
| Spring Data JPA | Latest |
| MySQL | 8+ |
| Lombok | Latest |
| Maven | Latest |
| Swagger/OpenAPI | Latest |
| Git | Version Control |
| GitHub | Repository Hosting |

---

## 📌 Features

### User Management
- User Registration
- User Profile Management
- Booking History
- Email Notifications

### Movie Management
- Add Movies
- Update Movie Details
- Remove Movies
- View Movie Listings

### Theater Management
- Add Theater
- Manage Theater Seats
- Update Theater Information

### Ticket Booking
- Browse Movies
- Select Theater
- Select Show Timings
- Seat Selection
- Ticket Booking

### Notifications
- Booking Confirmation Email
- Booking Updates
- Future Event Notifications

---

# 🏗️ Application Architecture

The project follows a layered architecture:

```text
Client
   |
   v
Controller Layer
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
JPA / Hibernate
   |
   v
MySQL Database
```

### Components

#### Controller Layer
Handles incoming REST API requests.

#### Service Layer
Contains business logic and validations.

#### Repository Layer
Communicates with the database using Spring Data JPA.

#### Database Layer
Stores movies, users, theaters, shows, and tickets.

---

# 📂 Project Structure

```text
src/main/java
|
├── Controllers
├── Services
├── Repositories
├── Models
├── DTOs
├── Transformers
├── Exceptions
└── Config
```

---

# ⚙️ Setup Instructions

## 1. Clone Repository

```bash
git clone https://github.com/<your-github-username>/bookmyshow-backend.git
```

## 2. Open Project

Open the project in IntelliJ IDEA.

---

## 3. Create Database

```sql
CREATE DATABASE bookmyshow;
```

---

## 4. Configure Database

Update:

```properties
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookmyshow

spring.datasource.username=root

spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

## 5. Build Project

```bash
mvn clean install
```

---

## 6. Run Application

```bash
mvn spring-boot:run
```

Application starts at:

```text
http://localhost:8080
```

---

# 📖 API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

The Swagger dashboard provides:

- API Endpoints
- Request Models
- Response Models
- Testing Interface

---

# ✅ Migration Roadmap

## Phase 1 - Codebase Stabilization

- Java 17 Baseline Verification
- Dependency Cleanup
- Exception Handling Improvements
- README Modernization

Status: ✅ Completed

---

## Phase 2 - Java 21 Migration

- Upgrade Project SDK
- Upgrade Build Tooling
- Spring Boot Upgrade
- Lombok Upgrade
- OpenAPI Upgrade

Status: 🚧 In Progress

---

## Phase 3 - Security

- Spring Security
- JWT Authentication
- Role-Based Authorization
- Password Encryption

Status: 📅 Planned

---

## Phase 4 - Production Readiness

- Docker
- Docker Compose
- Centralized Logging
- Health Checks
- Configuration Profiles

Status: 📅 Planned

---

## Phase 5 - Advanced Features

- Redis Caching
- Seat Locking Mechanism
- Concurrent Booking Protection
- Payment Gateway Integration
- Event Notifications

Status: 📅 Planned

---

# 🧪 Future Enhancements

- CI/CD using GitHub Actions
- Unit & Integration Tests
- Elasticsearch
- Kafka Event Streaming
- Cloud Deployment (Azure / AWS)

---

# 📸 Screenshots

### Application Flow

![Architecturecom/driver/bookMyShow/Images/BMSflowchart.jpg

### Swagger UI

![Swagger](src/main/java/com/driver/bookMyShowk-my-show%20API's.png

---

# 🙌 Acknowledgments

- Spring Boot
- Spring Data JPA
- MySQL
- OpenAPI / Swagger
- Maven

---

# 👨‍💻 Maintainer

### Venkatesh I

Backend Developer | Java | Spring Boot | REST APIs | System Design

This repository is based on an open-source BookMyShow implementation and has been customized, modernized, and enhanced for learning, interview preparation, and production-grade backend development.