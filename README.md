  # Books_store
Welcome to the Books Store, is an online book store project which allows the store to be managed by both the administrator and the customer.

# About
Books store is a project that allows registration and login of administrators and customers.

Admin functions :
- Add categories and books
- Update and delete categories, books
- Display caktegories, books, orders, registered customers
- Send orders

Customer functions:
- Add books to basket
- Display books, basket 
- Delete and update basket
- Order books
    
# Technologies used:
- Java 17
- Spring Boot
- Lombok
- Spring Security
- Hibernate
- JSON Web Token
- log4j
- Spring Data REST
- Database MySQL/H2

# Installation

## Prerequisites
Before you begin, ensure you have the following installed on your machine:
- Java 17: [Download and Install Java](https://adoptopenjdk.net/)
- MySQL: [Download and Install MySQL](https://dev.mysql.com/downloads/installer/)
- Maven: https://maven.apache.org/download.cgi
- Git: https://git-scm.com/downloads
- Postman: https://www.postman.com/downloads/

## Clone the Repository
git clone https://github.com/LukaszCh233/Books_store.git
cd Books_store

## Configure Database
spring.datasource.url=jdbc:mysql://localhost:3306/books_store
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8085

1. Open MySQL MySQL Workbench
2. Login to the administrator user of MySql
3. Use your username and password from configuration
4. Create database books_store
