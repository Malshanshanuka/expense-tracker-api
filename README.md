# Expense Tracker API

Backend RESTful API for Expense Tracker application built with Spring Boot 3, Spring Security, and PostgreSQL.

Frontend Repository: https://github.com/malshanshanuka/expense-tracker-frontend

## Features

- User authentication using JWT tokens and BCrypt password encryption
- Category management for personal expenses
- Expense creation, modification, deletion, and retrieval
- Monthly spending analytics and category breakdowns
- Automated monthly PDF report generation
- Global exception handling and DTO request validation
- Docker container support with Docker Compose setup

## Technology Stack

- Language: Java 17
- Framework: Spring Boot 3.3.2
- Security: Spring Security 6 & JJWT
- Database: PostgreSQL & Spring Data JPA
- PDF Export: iText 7 Core
- Build Tool: Maven
- Containerization: Docker & Docker Compose

## API Endpoints

### Authentication
- POST /api/auth/register - Register new account
- POST /api/auth/login - Authenticate user & get JWT token

### Expenses
- GET /api/expenses - Get all user expenses
- POST /api/expenses - Create new expense
- PUT /api/expenses/{id} - Update existing expense
- DELETE /api/expenses/{id} - Delete expense

### Categories
- GET /api/categories - Get user categories
- POST /api/categories - Create new category
- DELETE /api/categories/{id} - Delete category

### Reports
- GET /api/reports/monthly - Get monthly summary
- GET /api/reports/monthly/pdf - Download monthly PDF report

## Local Setup Instructions

### Prerequisites
- JDK 17
- Maven 3.9+
- PostgreSQL 15+

### Running with Maven

1. Clone the repository:
```bash
git clone https://github.com/malshanshanuka/expense-tracker-api.git
cd expense-tracker-api
```

2. Configure database credentials in `src/main/resources/application.properties` if needed.

3. Start the application:
```bash
./mvnw clean spring-boot:run
```

The API will be available at http://localhost:8080.

### Running with Docker Compose

Run the API and PostgreSQL together:
```bash
docker-compose up --build
```

## Author

Malshan Shanuka
