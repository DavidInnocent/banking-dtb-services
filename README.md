# Banking Platform Microservices

A modular banking platform built with Spring Boot and Spring Cloud following microservices architecture.

## 🏗️ Architecture Overview
![Architecture Diagram](https://res.cloudinary.com/ximmoz-corp/image/upload/v1746020080/images/service_communication.png)
```mermaid
graph TD
    A[API Gateway] --> B[Profile Service]
    A --> C[Store Service]
    A --> D[Payment Service]
    B --> F[(PostgreSQL)]
    C --> G[(PostgreSQL)]
    D --> H[(PostgreSQL)]
    D --> E
    E --> J[(RabbitMQ)]
    style A fill:#4CAF50,stroke:#388E3C
    style B fill:#2196F3,stroke:#1565C0
    style C fill:#FF9800,stroke:#F57C00
    style D fill:#9C27B0,stroke:#7B1FA2
    style E fill:#607D8B,stroke:#455A64

# Services

| Service            | Tech Stack               | Description          | Port | DB          |
|--------------------|--------------------------|----------------------|------|-------------|
| **Registry Service**  | Eureka Server           | Service discovery    | 8761 | -           |
| **Config Server**     | Spring Cloud Config     | Centralized config   | 8888 | Git         |
| **Profile Service**   | Spring Security, JWT    | User management      | 8081 | PostgreSQL  |
| **Store Service**     | Feign Client            | Product catalog      | 8082 | MySQL       |
| **Payment Service**   | Spring Data JPA         | Transactions         | 8083 | H2          |
| **Event Service**     | RabbitMQ, MongoDB       | Notifications        | 8084 | MongoDB     |

# Prerequisites

- Java 17+
- Docker 20.10+
- Maven 3.8+
- PostgreSQL 14
- MySQL 8
- RabbitMQ 3.9

# Installation

```bash
# Clone repository
git clone https://github.com/yourusername/banking-platform.git
cd banking-platform

# Build all services
mvn clean install

# Start infrastructure
docker-compose -f docker/docker-compose-infra.yml up -d