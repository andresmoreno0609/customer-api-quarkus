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

## 📂 Estructura de Paquetes

```
src/main/java/com/challenge/customers
│
├── application
│   ├── port/                 # Interfaces de entrada (Use Cases)
│   ├── service/              # Implementaciones de casos de uso
│   └── web/                  # Controladores REST (ClientResource)
│
├── domain
│   ├── model/                # Entidades JPA (Client, Catalogs)
│   ├── repository/           # Interfaces Panache
│   └── strategy/             # Implementación del patrón Strategy
│
└── infrastructure
    └── persistence/          # Adaptadores a base de datos (Panache)
```

---

## 🚀 Ejecución del proyecto

### ▶️ Ejecución local

```bash
./mvnw clean quarkus:dev
```

El servicio se levantará en:

```
http://localhost:8080
```

Base de datos H2 embebida:
```
jdbc:h2:mem:customers;DB_CLOSE_DELAY=-1
```

---

## 📘 Endpoints principales

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| `GET` | `/clients` | Lista todos los clientes |
| `GET` | `/clients/{id}` | Obtiene un cliente por su ID |
| `POST` | `/clients` | Crea un nuevo cliente |
| `PATCH` | `/clients/{id}` | Actualiza los datos de un cliente |
| `POST` | `/clients/{id}/activate` | Activa el cliente |
| `POST` | `/clients/{id}/inactivate` | Inactiva el cliente |
| `DELETE` | `/clients/{id}` | Elimina lógicamente un cliente |

---

## 🧩 Swagger y OpenAPI

### 📄 Documentación OpenAPI
```
http://localhost:8080/q/openapi
```

### 🧭 Swagger UI
```
http://localhost:8080/q/swagger-ui/
```

Permite probar los endpoints directamente desde el navegador.

---

## 🧑‍💻 Autor

**Andrés Raúl Moreno López**  
Desarrollador Backend | Java / Quarkus / Spring Boot  
📍 Colombia  
📧 [andresmoreno1991609@gmail.com]

