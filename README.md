# Finance Data Processing and Access Control Backend

A Spring Boot REST API backend for a finance dashboard system with JWT authentication and role-based access control.

## Tech Stack
- Java 17
- Spring Boot 3.5.13
- Spring Security + JWT
- Spring Data JPA
- H2 In-Memory Database
- Lombok

## Project Structure
src/main/java/finance_backend/
├── controller/    # REST API endpoints
├── model/         # User, Transaction entities
├── repository/    # Database access layer
├── security/      # JWT auth, filters, config
└── service/       # Business logic
## Roles & Access Control
| Role    | Permissions |
|---------|-------------|
| VIEWER  | View dashboard only |
| ANALYST | View records + summaries |
| ADMIN   | Full access - create, update, delete |

## API Endpoints

### Auth
- POST /api/auth/register - Register user
- POST /api/auth/login - Login and get JWT token

### Users (Admin only)
- GET /api/users - Get all users
- PATCH /api/users/{id}/role - Update user role
- PATCH /api/users/{id}/status - Activate/deactivate user

### Transactions
- GET /api/transactions - Get all (with filters)
- POST /api/transactions - Create (Admin)
- PUT /api/transactions/{id} - Update (Admin)
- DELETE /api/transactions/{id} - Soft delete (Admin)

### Dashboard
- GET /api/dashboard/summary - Total income, expenses, net balance, category breakdown

## Setup & Run
```bash
# Clone the repo
git clone https://github.com/Harshhh-123/finance-backend.git

# Run the app
./mvnw spring-boot:run
Server starts at http://localhost:8080
Assumptions
H2 in-memory database used for simplicity
JWT tokens expire after 24 hours
Soft delete used for transactions
Roles: VIEWER, ANALYST, ADMIN
