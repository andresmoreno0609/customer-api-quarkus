# 🧩 Customers API - Reto Técnico Quarkus

## 📖 Contexto del Reto

Este proyecto corresponde al **reto técnico para la posición de Java Backend Developer**, enfocado en el uso de **Quarkus**, **Hibernate Panache**, **H2 embebida** y la aplicación de buenas prácticas de diseño y arquitectura limpia.  
El objetivo es exponer un conjunto de endpoints REST que permitan gestionar clientes (`Client`), aplicando validaciones de negocio específicas por país mediante el **Patrón Strategy**.

---

## ⚙️ Tecnologías utilizadas

| Tecnología / Librería | Descripción |
|------------------------|-------------|
| **Java 17** | Versión del JDK usada para desarrollo |
| **Quarkus 3.15.7** | Framework principal para microservicios nativos de Java |
| **Hibernate ORM Panache** | Simplifica el acceso a datos con un modelo de entidades reactivo |
| **H2 Database** | Base de datos en memoria para pruebas locales |
| **Jakarta Persistence (JPA)** | Gestión de entidades |
| **JUnit 5 + Mockito** | Pruebas unitarias y de servicios |
| **RestAssured** | Pruebas de integración HTTP |
| **SmallRye OpenAPI** | Documentación OpenAPI/Swagger generada automáticamente |
| **Lombok** | Reducción de boilerplate (getters, setters, builders, etc.) |

---

## 🧱 Arquitectura

El proyecto sigue el enfoque de **Arquitectura Hexagonal (Ports & Adapters)**:

src/main/java/com/challenge/customers
│
├── application
│   ├── port/
│   ├── service/
│   └── web/
│
├── domain
│   ├── model/
│   ├── repository/
│   └── strategy/
│
└── infrastructure
└── persistence/
