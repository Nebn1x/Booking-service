# 🏠 Booking Service API

The **Booking Service API** is a powerful RESTful backend built with Java and Spring Boot, designed to simplify accommodation rentals. Users can book properties, admins can manage listings and availability, and the system integrates with Stripe for secure payments and Telegram for real-time notifications. This scalable solution modernizes property rental management.


## ✨ Features
- **CRUD Operations**: Create, read, update, and delete accommodations, bookings, and user profiles.
- **Secure Payments**: Process payments via Stripe with success/cancel handling.
- **Real-Time Notifications**: Send booking updates via Telegram.
- **Role-Based Access**: Supports admin and customer roles with JWT authentication.
- **Database Migrations**: Automate schema updates with Liquibase.
- **Pagination**: Efficiently retrieve paginated lists of accommodations and bookings.

## 🧩 Technologies Used
- **Java 21**: Core programming language
- **Spring Boot 3.4.1**: Framework for rapid development
- **Spring Security 6.4.4 + JWT 0.12.6**: Authentication and authorization
- **Liquibase**: Database schema management
- **MySQL 8.2.0**: Relational database
- **Stripe API 24.0.0**: Payment processing
- **Telegram Bot API 6.9.7.1**: Instant notifications
- **Docker & Docker Compose**: Containerized deployment
- **Testcontainers 1.20.6 + JUnit 5**: Unit and integration testing
- **Springdoc OpenAPI 2.8.1 (Swagger)**: API documentation
- **GitHub Actions**: CI/CD automation
- **Checkstyle**: Code style enforcement
- **Lombok 1.18.34**: Boilerplate reduction
- **MapStruct 1.6.3**: Entity-DTO mapping

## 🗂️ Project Structure
The project uses a layered Spring Boot architecture:
- **Controllers**: Handle HTTP requests
- **Services**: Business logic
- **Repositories**: Database interactions
- Source code: `src/main`
- Tests: `src/test`

## 🛠️ Setup Instructions

### Prerequisites
- **Java 21**
- **Maven** (or use the included Maven wrapper)
- **Docker** (for containerized deployment)
- **MySQL** (running locally or via Docker)

### Configuration
1. Clone the repository:
   ```bash
   git clone git@github.com:Nebn1x/Booking-service.git
   cd Booking-service
   ```

2. Configure environment variables in `.env`:

   ```bash
   ### MySQL Configuration ###
   MYSQLDB_USER=booking_user
   MYSQLDB_ROOT_PASSWORD=securepass123
   MYSQLDB_DATABASE=booking_service_db
   MYSQLDB_LOCAL_PORT=3307
   MYSQLDB_DOCKER_PORT=3306

   ### Application Ports ###
   SPRING_LOCAL_PORT=8081
   SPRING_DOCKER_PORT=8080
   DEBUG_PORT=5005

   ### JWT Configuration ###
   JWT_SECRET=your_jwt_secret_key
   JWT_EXPIRATION_MS=900000

   ### Stripe Configuration ###
   STRIPE_SECRET_KEY=your_stripe_api_key
   STRIPE_SUCCESS_URL=http://localhost:8080/payments/success/{CHECKOUT_SESSION_ID}
   STRIPE_CANCEL_URL=http://localhost:8080/payments/cancel/{CHECKOUT_SESSION_ID}

   ### Telegram Configuration ###
   BOT_NAME=your_telegram_bot_name
   BOT_TOKEN=your_telegram_bot_token
   TELEGRAM_SECRET=your_telegram_secret_key
   ```

3. Build the project:
   ```bash
   ./mvnw clean package
   ```

4. Build the Docker image:
   ```bash
   docker compose build
   ```

5. Run the application:
   ```bash
   docker compose up
   ```

6. Access the API at `http://localhost:8081`.

### Note
- Liquibase will initialize the database schema and seed data on the first run.

## 🧪 Example Requests

### Create an Accommodation
```http
POST http://localhost:8081/properties
Content-Type: application/json

{
    "name": "Cozy Apartment",
    "price": 100,
    "available": true
}
```

**Response (HTTP 201)**:
```json
{
    "id": 1,
    "name": "Cozy Apartment",
    "price": 100,
    "available": true
}
```

### Create a Booking
```http
POST http://localhost:8081/bookings
Content-Type: application/json

{
    "propertyId": 1,
    "userId": 1,
    "startDate": "2025-10-01",
    "endDate": "2025-10-05"
}
```

**Response (HTTP 201)**:
```json
{
    "id": 1,
    "propertyId": 1,
    "userId": 1,
    "startDate": "2025-10-01",
    "endDate": "2025-10-05",
    "status": "CONFIRMED"
}
```

## 📄 Testing
Unit and integration tests cover controllers, services, and repositories. Run tests with:
```bash
./mvnw test
```

Tests validate CRUD operations, payment processing, and notification functionality.

## 👤 Demo Credentials
Test authentication and role-based access with these accounts:

- **Admin Account**  
  - Email: `admin@example.com`  
  - Password: `securepass123`  
  - Roles: `ADMIN`

- **Customer Account**  
  - Email: `customer@example.com`  
  - Password: `securepass123`  
  - Roles: `CUSTOMER`
