# 🔗 URL Shortener

A full-stack URL Shortener built with **Java, Spring Boot, MySQL, HTML, CSS, and JavaScript**.

The application allows users to convert long URLs into short, shareable links and provides click analytics for each shortened URL.

---

## ✨ Features

- 🔗 Generate short URLs
- 🎯 Custom aliases
- 🔄 Redirect short URLs to original URLs
- 📊 Click statistics
- ⏳ URL expiration
- 🛡️ URL validation
- 🚦 API rate limiting
- ❌ Global exception handling
- 💾 MySQL database persistence
- 🧪 Unit and controller tests
- 📖 Swagger/OpenAPI documentation
- 🌐 Responsive frontend
- 📋 Copy shortened URL to clipboard

---

## 🏗️ Architecture

```text
                    ┌─────────────────────┐
                    │      Frontend       │
                    │   HTML / CSS / JS   │
                    └──────────┬──────────┘
                               │
                               │ REST API
                               ▼
                    ┌─────────────────────┐
                    │    Spring Boot      │
                    │      Backend        │
                    └──────────┬──────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
              ▼                ▼                ▼
        ┌──────────┐    ┌────────────┐   ┌────────────┐
        │Controller│    │  Service   │   │   Filter   │
        └──────────┘    └────────────┘   │Rate Limit  │
              │                │           └────────────┘
              │                ▼
              │         ┌────────────┐
              │         │ Repository │
              │         └─────┬──────┘
              │               │
              ▼               ▼
        ┌──────────────────────────┐
        │          MySQL           │
        └──────────────────────────┘

🛠️ Tech Stack
Backend
Java 21
Spring Boot 3.5.5
Spring Web
Spring Data JPA
Hibernate
Maven
MySQL

Frontend
HTML5
CSS3
JavaScript
Fetch API

Testing
JUnit 5
Mockito
Spring Boot Test
MockMvc

Documentation
Swagger / OpenAPI

url-shortener/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/sidsng/urlshortener/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── exception/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   └── pom.xml
│
├── frontend/
│   ├── assets/
│   ├── css/
│   │   └── style.css
│   ├── js/
│   │   └── script.js
│   └── index.html
│
├── docs/
│   ├── api.md
│   └── architecture.md
│
├── LICENSE
├── README.md
└── .gitignore

Running Tests

From the backend directory:
mvn clean test 

The project includes tests for:
URL shortening
URL validation
Redirect functionality
Click counting
REST controllers
Error handling

🔒 Security & Reliability
The application includes:
URL validation
Duplicate short-code protection
Custom alias protection
API rate limiting
Global exception handling
Database persistence
CORS configuration

🎯 Future Improvements
Possible future enhancements:
User authentication
User-specific URL management
QR code generation
Advanced analytics dashboard
Redis caching
Docker deployment
Cloud deployment
Custom domains
URL management dashboard