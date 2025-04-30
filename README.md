# Banking Platform Microservices

A modular banking platform built with Spring Boot and Spring Cloud following microservices architecture.

## 🏗️ Architecture Overview

```mermaid
graph TD
    A[API Gateway] --> B[Profile Service]
    A --> C[Store Service]
    A --> D[Payment Service]
    A --> E[Event Service]
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