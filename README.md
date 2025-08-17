# Demo Task API Tests

This repository contains a simple REST API controller for managing tasks (**TaskController**) and a set of integration tests for it.  

---

## ⚙️ Technologies

**Java 17+** – modern Java version.  
**Maven** – for dependency management and project build.  
**Spring Boot** – REST API implemented in TaskController.  
**JUnit 5** – testing framework.  
**RestAssured** – for sending HTTP requests and validating responses.  
**AssertJ** – for readable and convenient assertions.

---

## 📝 API Overview

The TaskController provides basic CRUD operations for tasks:

| HTTP Method | Endpoint       | Description                  |
|------------|----------------|------------------------------|
| POST       | /tasks        | Create a new task            |
| GET        | /tasks/{id}   | Retrieve a task by ID        |
| PUT        | /tasks/{id}   | Update an existing task      |
| DELETE     | /tasks/{id}   | Delete a task by ID          |

Each task typically has:

id – unique identifier
title – task title
description – task description
status – task status (e.g., NEW, IN_PROGRESS, DONE)

---

## 🚀 How to Run Tests

1. Make sure you have **Java 17+** and **Maven** installed.
2. Clone the repository:

```bash
git clone <repository_url>
cd <repository_folder>
```

3. Run the Spring Boot application (if the tests require a running server):


```bash
mvn spring-boot:run
```

4. Run the integration tests with Maven:


```bash
`mvn test`
```

The tests cover the full CRUD lifecycle:

1. **Create** a new task
2. **Verify** that the task exists and has correct data
3. **Update** the task and verify changes
4. **Delete** the task and ensure it’s removed