# User Service Spring

## Description
User Service Spring is a microservice developed using Spring Boot that manages users within the application. This service provides an API for creating, updating, deleting, and retrieving users.

## Technologies
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Running the Project
To run the project, follow these steps:
1. Clone the repository:
   ```bash
   git clone <URL>
2. Navigate to the project directory:
   ```bash
   cd task_service_spring
4. Start the application:
   ```bash
   ./mvnw spring-boot:run
5. The User Service will be available at http://localhost:8080/users.
   
## Endpoints
GET /users
Retrieve a list of all users.

GET /users/{id}
Get user information by identifier.

POST /users
Create a new user.
The request body should contain JSON with user information.

PUT /users/{id}
Update user information by identifier.
The request body should contain JSON with the updated user information.

DELETE /users/{id}
Delete a user by identifier.

---

You can customize these files according to your project structure, specific requirements, and implementation details. If you need additional sections such as "Installation" or "Configuration," please let me know!
