# Reviews API 
Supports operations for writing reviews and listing reviews for a product but with no sorting or filtering.

### Prerequisites
MySQL and MongoDB need to be installed and configured. Instructions provided separately.

### Feature
* Product details will be stored on MySQL database 
* Using Flyway for database migration. It helps us to easily revert back the sql changes
* Product reviews will be stored on MongoDB

### How to run the application
mvn springboot:run

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
