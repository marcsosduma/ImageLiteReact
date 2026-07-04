# 📸 ImageLite

**A full-stack application developed as part of a hands-on course covering React integration with Spring Boot.**

This project demonstrates how to build, containerize, and orchestrate a modern web application, featuring image management capabilities, secure authentication, and a robust microservices-based architecture.

## 📝 Project Overview
ImageLite is a system designed to handle image uploads, storage, and retrieval. It showcases the synergy between a powerful Java-based backend and a reactive, performant frontend using Next.js, all managed within a professional Dockerized environment.

## 🛠 Tech Stack

### Backend
* **Java 21**
* **Spring Boot 3.5**
* **Spring Data JPA / Hibernate**
* **Spring Security (JWT)** for authentication and authorization
* **PostgreSQL 16** (Relational Database)

### Frontend
* **React**
* **Next.js** (App Router)
* **Tailwind CSS**

### DevOps & Infrastructure
* **Docker & Docker Compose** for container orchestration
* **Multi-stage Docker builds** for optimized image sizes
* **Healthchecks** to manage service startup dependencies
* **Bridge Networking** for secure inter-container communication

## 🏗 Architecture
The system is composed of integrated services managed by Docker Compose:
1.  **`imageliteapi`**: A RESTful API built with Spring Boot to handle business logic and data persistence.
2.  **`db`**: A PostgreSQL 16 instance for metadata storage.
3.  **`app_react`**: A client-side interface built with Next.js.
4.  **`pgadmin`**: A GUI tool for efficient PostgreSQL database management.

## ⚙️ Key Technical Achievements
* **Container Orchestration:** Implemented `depends_on` with `service_healthy` conditions to ensure the API and application only start once the database is fully ready to accept connections.
* **Environment Management:** Utilized Spring Profiles to decouple development and production configurations.
* **Build Optimization:** Leveraged multi-stage Docker builds to ensure small, efficient production images.
* **Networking:** Successfully resolved Docker networking challenges, ensuring proper communication between containers using service discovery instead of `localhost`.

## 🚀 How to Run
1. Clone the repository:
   ```bash
   git clone <your-repo-link>