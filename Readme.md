# Hotel Management System

## 🏨 Overview

This is a hotel management system built using Java (Spring framework) and a **MICROSERVICE ARCHITECTURE**.
This application is built for managing hotel operations such as booking rooms, reserving rooms, ordering room service etc.
It also has role-based authorization for different users.
We have an **ADMIN**, **MODERATOR** AND **GUEST** ROLE.

We are using JWT authentication for authorization and authentication.

In this doc, I'll be explaining the architecture of the application and how each module
or service connects to each other. I'll aslo be giving endpoints of the application for more details
about each service you can view the each module readme for more details in that corresponding folder.



## 🏗️ Architecture

### Microservices
```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway (8080)                        │
│                    Single Entry Point for All Requests           │
└────────────┬────────────────────────────────────────────────────┘
             │
             ├─────────────────────────────────────────────────────┐
             │                                                     │
             ▼                                                     ▼
┌────────────────────────┐                         ┌────────────────────────┐
│   Service Registry     │                         │    Config Server       │
│   (Eureka - 8761)      │◄────────────────────────┤    (Optional)          │
│   Service Discovery    │                         │    Centralized Config  │
└────────────────────────┘                         └────────────────────────┘
             │
             │ (All services register here)
             │
    ┌────────┴────────┬───────────────┬
    │                 │               │                
    ▼                 ▼               ▼                
┌──────────┐   ┌──────────┐   ┌──────────┐   
│   Auth   │   │   Room   │   │   Food   │   
│ Service  │   │ Booking  │   │  Order   │   
│  (8081)  │   │ Service  │   │ Service  │  
│          │   │  (8082)  │   │  (8083)  │  
└──────────┘   └──────────┘   └──────────┘   
     │              │               │
     │              │               │
     ▼              ▼               ▼
┌──────────┐   ┌──────────┐   ┌──────────┐
│PostgreSQL│   │PostgreSQL│   │PostgreSQL│
│  Auth DB │   │Booking DB│   │ Food DB  │
└──────────┘   └──────────┘   └──────────┘
```

### Service Breakdown

1. **Service Registry (Eureka Server)** - Port 8761
    - Service discovery and registration
    - Health monitoring of all microservices
    - Load balancing support

2. **API Gateway** - Port 8080
    - Single entry point for all client requests
    - Route requests to appropriate microservices
    - JWT token validation
    - Rate limiting and request logging

3. **Auth Service** - Port 8066
    - User registration and authentication
    - JWT token generation and validation
    - User management
    - Role-based access control (RBAC)

4. **Room Booking Service** - Port 8062
    - Room inventory management
    - Room availability tracking
    - Booking creation and management
    - Room pricing management

5. **Food Order Service** - Port 8064
    - Menu management
    - Food order processing
    - Room service orders
    - Restaurant orders


## 🔐 Security & Authentication

### JWT Authentication

All services use JWT (JSON Web Token) for stateless authentication:
```
Client → Login (Auth Service) → JWT Token Generated
       ↓
Token stored in localStorage/cookies
       ↓
Subsequent requests include: Authorization: Bearer <token>
       ↓
Each service validates token using shared secret key
```

### Role-Based Access Control (RBAC)

| Role | Permissions |
|------|-------------|
| **GUEST** | - View available rooms<br>- Create room bookings<br>- Place food orders for their room<br>- View own bookings and orders |
| **MODERATOR** | - All GUEST permissions<br>- Update room availability<br>- Modify booking duration<br>- Receive and process food orders<br>- Place restaurant orders<br>- Mark orders as delivered |
| **ADMIN** | - All MODERATOR permissions<br>- Manage rooms (create, update, delete)<br>- Update room pricing<br>- Manage menu items<br>- Update food pricing<br>- View analytics and reports |

## 🚀 Technologies Used

### Backend
- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Cloud **
    - Spring Cloud Gateway
    - Spring Cloud Netflix Eureka
    - Spring Cloud OpenFeign
- **Spring Security** with JWT
- **Spring Data JPA**
- **PostgreSQL** (Production)
- **H2 Database** (Development/Testing)

### Message Queue (Optional)
- **Apache Kafka** or **RabbitMQ** for asynchronous communication

### Monitoring & Documentation
- **Spring Boot Actuator**
- **Swagger/OpenAPI** for API documentation
- **Sleuth + Zipkin** for distributed tracing
- **Prometheus + Grafana** for metrics (Optional)

### Build & Deployment
- **Maven**
- **Docker & Docker Compose**

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 14+**
- **Docker & Docker Compose** (for containerized deployment)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/hotel-management-system.git
cd hotel-management-system
```

### 2. Database Setup

Create PostgreSQL databases for each service:
```sql
CREATE DATABASE hotel_auth_db;
CREATE DATABASE hotel_booking_db;
CREATE DATABASE hotel_food_db;
```

### 3. Configure Environment Variables

Create `.env` file or update `application.yml` in each service:
```properties
# Database Configuration
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=yourpassword

# JWT Configuration (MUST be the same across all services)
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000

# Eureka Configuration
EUREKA_SERVER_URL=http://localhost:8761/eureka/
```

### 4. Build the Project
```bash
# Build all modules
mvn clean install

# Or build individual services
cd service-registry
mvn clean install

cd ../auth-service
mvn clean install

cd ../room-booking-service
mvn clean install

cd ../food-order-service
mvn clean install
```

### 5. Run Services

**Option A: Run individually (for development)**
```bash
# Terminal 1 - Service Registry
cd service-registry
mvn spring-boot:run

# Terminal 2 - Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 3 - Room Booking Service
cd room-booking-service
mvn spring-boot:run

# Terminal 4 - Food Order Service
cd food-order-service
mvn spring-boot:run
```

**Option B: Using Docker Compose**
```bash
docker-compose up -d
```

### 6. Verify Services are Running

- **Eureka Dashboard**: http://localhost:8761
- **Auth Service Swagger**: http://localhost:8066/swagger-ui.html
- **Room Booking Service Swagger**: http://localhost:8062/swagger-ui.html
- **Food Order Service Swagger**: http://localhost:8064/swagger-ui.html

## 📡 API Endpoints

### Authentication Service (Port 8081)

#### Public Endpoints
```http
POST /api/auth/register          # Register new user
POST /api/auth/login             # User login
POST /api/auth/refresh           # Refresh JWT token
```

#### Protected Endpoints
```http
GET  /api/auth/me                # Get current user info
PUT  /api/auth/profile           # Update user profile
PUT  /api/auth/change-password   # Change password

# Admin Only
GET    /api/auth/users           # Get all users
GET    /api/auth/users/{id}      # Get user by ID
PUT    /api/auth/users/{id}/role # Update user role
DELETE /api/auth/users/{id}      # Delete user
```

### Room Booking Service (Port 8082)

#### Room Management
```http
GET    /api/rooms                    # Get all rooms (filter by type, price, availability)
GET    /api/rooms/{id}               # Get room details
POST   /api/rooms                    # Create room (ADMIN)
PUT    /api/rooms/{id}               # Update room (ADMIN)
DELETE /api/rooms/{id}               # Delete room (ADMIN)
PUT    /api/rooms/{id}/pricing       # Update room pricing (ADMIN)
GET    /api/rooms/available          # Get available rooms
```

#### Booking Management
```http
GET    /api/bookings                 # Get bookings (filtered by role)
GET    /api/bookings/{id}            # Get booking details
POST   /api/bookings                 # Create booking (GUEST)
PUT    /api/bookings/{id}/duration   # Update booking duration (MODERATOR/ADMIN)
PUT    /api/bookings/{id}/status     # Update booking status (MODERATOR/ADMIN)
DELETE /api/bookings/{id}            # Cancel booking
GET    /api/bookings/my-bookings     # Get user's bookings (GUEST)
```

### Food Order Service (Port 8083)

#### Menu Management
```http
GET    /api/menu                     # Get all menu items
GET    /api/menu/{id}                # Get menu item details
GET    /api/menu/category/{category} # Get items by category
POST   /api/menu                     # Create menu item (ADMIN)
PUT    /api/menu/{id}                # Update menu item (ADMIN)
DELETE /api/menu/{id}                # Delete menu item (ADMIN)
PUT    /api/menu/{id}/pricing        # Update item pricing (ADMIN)
PUT    /api/menu/{id}/availability   # Update availability (ADMIN/MODERATOR)
```

#### Order Management
```http
GET    /api/orders                   # Get orders (filtered by role)
GET    /api/orders/{id}              # Get order details
POST   /api/orders/room-service      # Place room service order (GUEST)
POST   /api/orders/restaurant        # Place restaurant order (MODERATOR/ADMIN)
PUT    /api/orders/{id}/status       # Update order status (MODERATOR/ADMIN)
DELETE /api/orders/{id}              # Cancel order
GET    /api/orders/my-orders         # Get user's orders (GUEST)
GET    /api/orders/pending           # Get pending orders (MODERATOR/ADMIN)
```

## 🧪 Testing the Application

### 1. Register a User
```bash
curl -X POST http://localhost:8066/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "phoneNumber": "1234567890",
    "role": "USER"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8066/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "USER",
  "username": "john_doe"
}
```

### 3. Use Token for Protected Endpoints
```bash
# Get available rooms
curl http://localhost:8062/api/rooms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Create a booking
curl -X POST http://localhost:8062/api/bookings \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomId": 1,
    "checkInDate": "2025-02-01",
    "checkOutDate": "2025-02-05",
    "numberOfGuests": 2
  }'

# Place a food order
curl -X POST http://localhost:8083/api/orders/room-service \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "101",
    "items": [
      {
        "menuItemId": 1,
        "quantity": 2
      }
    ]
  }'
```

## 📊 Database Schema

### Auth Service Database
```sql
-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Room Booking Service Database
```sql
-- Rooms Table
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    view_type VARCHAR(50),
    floor_number INTEGER,
    max_occupancy INTEGER NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    amenities JSONB,
    description TEXT,
    images JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings Table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_number VARCHAR(20) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL REFERENCES rooms(id),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INTEGER NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    special_requests TEXT,
    created_by BIGINT NOT NULL,
    modified_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Food Order Service Database
```sql
-- Menu Items Table
CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    item_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders Table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(20) UNIQUE NOT NULL,
    order_type VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    room_number VARCHAR(10),
    table_number VARCHAR(10),
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    special_instructions TEXT,
    placed_by BIGINT NOT NULL,
    processed_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivered_at TIMESTAMP
);

-- Order Items Table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id),
    item_name VARCHAR(100) NOT NULL,
    item_price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL
);
```

## 🐳 Docker Deployment

### docker-compose.yml
```yaml
version: '3.8'

services:
  # PostgreSQL Databases
  postgres-auth:
    image: postgres:14
    environment:
      POSTGRES_DB: hotel_auth_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres-auth-data:/var/lib/postgresql/data

  postgres-booking:
    image: postgres:14
    environment:
      POSTGRES_DB: hotel_booking_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5433:5432"
    volumes:
      - postgres-booking-data:/var/lib/postgresql/data

  postgres-food:
    image: postgres:14
    environment:
      POSTGRES_DB: hotel_food_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5434:5432"
    volumes:
      - postgres-food-data:/var/lib/postgresql/data

  # Service Registry
  service-registry:
    build: ./service-registry
    ports:
      - "8761:8761"
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  # Auth Service
  auth-service:
    build: ./auth-service
    ports:
      - "8081:8081"
    depends_on:
      - postgres-auth
      - service-registry
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-auth:5432/hotel_auth_db
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/

  # Room Booking Service
  room-booking-service:
    build: ./room-booking-service
    ports:
      - "8082:8082"
    depends_on:
      - postgres-booking
      - service-registry
      - auth-service
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-booking:5432/hotel_booking_db
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/

  # Food Order Service
  food-order-service:
    build: ./food-order-service
    ports:
      - "8083:8083"
    depends_on:
      - postgres-food
      - service-registry
      - auth-service
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-food:5432/hotel_food_db
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/

volumes:
  postgres-auth-data:
  postgres-booking-data:
  postgres-food-data:
```

### Running with Docker
```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## 📁 Project Structure
```
hotel-management-system/
├── common-security/                 # Shared security components
│   ├── src/main/java/com/hotel/common/security/
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── BaseSecurityConfig.java
│   └── pom.xml
│
├── service-registry/                # Eureka Server
│   ├── src/main/java/com/hotel/registry/
│   │   └── ServiceRegistryApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── auth-service/                    # Authentication & User Management
│   ├── src/main/java/com/hotel/auth/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── security/
│   │   └── AuthServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── room-booking-service/            # Room & Booking Management
│   ├── src/main/java/com/hotel/booking/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── client/
│   │   ├── config/
│   │   └── RoomBookingServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── food-order-service/              # Menu & Order Management
│   ├── src/main/java/com/hotel/food/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── client/
│   │   ├── config/
│   │   └── FoodOrderServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── docker-compose.yml
├── pom.xml                          # Parent POM
└── README.md
```

## 🔧 Configuration

### application.yml Template

Each service should have similar configuration:
```yaml
spring:
  application:
    name: your-service-name
  
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: ${POSTGRES_USER:postgres}
    password: ${POSTGRES_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

# JWT Configuration
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: ${JWT_EXPIRATION:86400000}

# Server Configuration
server:
  port: ${PORT:8081}

# Eureka Configuration
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true

# Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

## 🔍 Monitoring & Health Checks

### Health Check Endpoints
```bash
# Service Registry
curl http://localhost:8761/actuator/health

# Auth Service
curl http://localhost:8081/actuator/health

# Room Booking Service
curl http://localhost:8082/actuator/health

# Food Order Service
curl http://localhost:8083/actuator/health
```

### Metrics
```bash
curl http://localhost:8081/actuator/metrics
curl http://localhost:8081/actuator/metrics/jvm.memory.used
```

## 🐛 Troubleshooting

### Common Issues

1. **Service not registering with Eureka**
    - Check Eureka server is running on port 8761
    - Verify `eureka.client.service-url.defaultZone` in application.yml
    - Check network connectivity

2. **JWT Authentication failing**
    - Ensure JWT secret is the same across all services
    - Check token expiration
    - Verify Authorization header format: `Bearer <token>`

3. **Database connection errors**
    - Verify PostgreSQL is running
    - Check database credentials
    - Ensure database exists

4. **Port already in use**
```bash
   # Find process using port
   lsof -i :8081
   
   # Kill process
   kill -9 <PID>
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Your Name - Initial work - [YourGitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- Spring Boot team for excellent documentation
- Netflix OSS for Eureka
- JWT.io for JWT resources
- PostgreSQL community

## 📞 Support

For support, email your-email@example.com or open an issue in the repository.

## 🗺️ Roadmap

- [ ] Add payment gateway integration
- [ ] Implement real-time notifications with WebSocket
- [ ] Add analytics dashboard
- [ ] Implement caching with Redis
- [ ] Add API rate limiting
- [ ] Implement CI/CD pipeline
- [ ] Add comprehensive integration tests
- [ ] Create mobile app
- [ ] Add multi-language support
- [ ] Implement audit logging

---

**Built with ❤️ using Spring Boot & Microservices Architecture**


