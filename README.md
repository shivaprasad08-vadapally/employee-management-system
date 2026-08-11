# Employee Management System

A secure RESTful Employee Management System built using Spring Boot, Spring Security, JWT authentication, Spring Data JPA, and MySQL.

## Features

- Employee CRUD operations
- JWT-based authentication
- Role-based authorization
- ADMIN and USER roles
- Employee search and filtering
- Pagination and sorting
- Input validation
- Global exception handling
- Duplicate email validation
- Swagger/OpenAPI documentation
- MySQL database integration

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Hibernate
- Maven
- Swagger / OpenAPI
- Lombok

## Security

### ADMIN

- Create employee
- View employees
- Update employee
- Delete employee

### USER

- View employees
- Search and filter employees

Authentication is implemented using JWT tokens.

## API Endpoints

### Authentication

```text
POST /api/auth/login