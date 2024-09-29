# Blog Service

This project is the **Blog Service** component of the **Voters Voice Amendment (VVA)** system. It provides functionality for managing blog posts and comments related to VVA initiatives. This microservice handles operations like creating, updating, and retrieving blog posts and user comments.

## Features

- **Blog Posts**:
    - Create, edit, and delete blog posts.
    - Retrieve a list of all blog posts or a single blog post.

- **Comments**:
    - Allow users to add comments to blog posts.
    - Retrieve comments associated with specific blog posts.

- **User Authentication**:
    - User-related actions (like commenting) are authenticated through JWTs issued by the VVA's authentication system.

## Technologies Used

- **Spring Boot** 3.3.4 (Java 23)
    - **Spring Web**: For building RESTful APIs.
    - **Spring Data JPA**: For interacting with the PostgreSQL database.
    - **PostgreSQL**: As the relational database to store blog posts and comments.

## Project Setup

### Prerequisites

Before running this project, ensure you have the following installed:

- **Java 23 or higher**
- **Maven**
- **PostgreSQL** (If running locally)

### Build and Run the Project

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/blogservice.git
    cd blogservice
    ```

2. **Build the project**:
    ```bash
    mvn clean package
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

The application will start on [http://localhost:8080](http://localhost:8080) by default.

### Dockerization

This service is containerized using Docker. You can build and run the Docker container as follows:

1. **Build the Docker image**:
    ```bash
    docker build -t blogservice .
    ```

2. **Run the Docker container**:
    ```bash
    docker run -p 8080:8080 blogservice
    ```

### Database Configuration

The project is configured to use **PostgreSQL**. You can set the database credentials in the `application.properties` or use environment variables to override them.

**Example properties**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_service
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
