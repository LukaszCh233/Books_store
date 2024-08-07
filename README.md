  # Books_store
Welcome to the Books Store, an online platform that enables the
management and purchase of books by both administrators and customers.

# About
Books Store is a project developed in Java and Spring Boot, offering
two modes of use: one for bookstore owners (administrators) and
the other for customers.

### Admin Functions:

**Register/Login:**
- Access the admin panel through the login page

**Category Management:**
- Add new categories
- Update existing categories
- Delete categories

**Book Management:**
- Add new books
- Update existing books
- Delete books

**Display Information:**
- View all categories
- View all books
- View all orders
- View all registered customers

**Order Management:**
- Process and send orders to customers

### Customer Functions:
**Register/Login:**
- Sign up or log in to access your account

**Shopping:**
- View all categories
- View all books
- Browse and view available books
- Add books to the basket
- View basket contents
- Update quantities of books in the basket
- Remove books from the basket
- Place orders for books
    
# Technologies used:
- **Java 17**: The programming language used for development.
- **Spring Boot**: Framework for building the application.
- **Spring Data JPA**: For database interactions.
    - **Spring Security**: For securing the application.
    - **Spring Boot Web**: For building web applications.
    - **Spring Boot Starter Test**: For testing the application.
- **Lombok**: To reduce boilerplate code by generating getters, setters, etc.
- **Hibernate**: ORM framework used with Spring Data JPA.
- **JSON Web Token (JWT)**: For authentication and authorization.
- **MySQL**: The relational database management system used.
- **JUnit**: For unit testing.
- **Spring Data REST WebMVC**: For creating RESTful APIs.
- **Maven**
- **IntelliJ IDEA**
- **Postman**

# Installation

## Prerequisites
Before you begin, ensure you have the following installed on your machine:
- **Postman**: https://www.postman.com/downloads/
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)

## How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/LukaszCh233/Books_store.git
cd quiz-world
```

### 2. Build and Start the Containers

```bash
docker-compose up --build
```
### 3. Stopping the Containers

```bash
docker-compose down
```