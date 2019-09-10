# eCommerce Application

This project demonstrates how to add proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way using JWT. 

## Project Details
This project is written in Java using Spring Boot, Hibernate ORM, and the H2 database. H2 is an in memory database, so if you need to retry something, every application startup is a fresh copy.

To use the template, import it in the IDE of your choice as a Spring Boot application. Where required, this readme assumes the eclipse IDE.

Once the project is set up, you will see 5 packages:

* demo - this package contains the main method which runs the application

* model.persistence - this package contains the data models that Hibernate persists to H2. There are 4 models: Cart, for holding a User's items; Item , for defining new items; User, to hold user account information; and UserOrder, to hold information about submitted orders. Looking back at the application “demo” class, you'll see the `@EntityScan` annotation, telling Spring that this package contains our data models

* model.persistence.repositories - these contain a `JpaRepository` interface for each of our models. This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods. Look through them and see the methods that have been declared. Looking at the application “demo” class, you’ll see the `@EnableJpaRepositories` annotation, telling Spring that this package contains our data repositories.

* model.requests - this package contains the request models. The request models will be transformed by Jackson from JSON to these models as requests are made. Note the `Json` annotations, telling Jackson to include and ignore certain fields of the requests. You can also see these annotations on the models themselves.

* controllers - these contain the api endpoints for our app, 1 per model. Note they all have the `@RestController` annotation to allow Spring to understand that they are a part of a REST API

In resources, you'll see the application configuration that sets up our database and Hibernate, It also contains a data.sql file with a couple of items to populate the database with. Spring will run this file every time the application starts

In eclipse, you can right click the project and click  “run as” and select Spring Boot application. The application should tell you it’s starting in the console view. Once started, using a REST client, such as Postman, explore the APIs.

Some examples are as below:
To create a new user for example, you would send a POST request to:
http://localhost:8080/api/user/create with an example body like 

```
{
    "username": "test"
}
```


and this would return
```
{
    "id" 1,
    "username": "test"
}
```



## How to login


Use Spring's default /login endpoint to login like so

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

and that should, if those are valid credentials, return a 200 OK with an Authorization header which looks like "Bearer <data>" this "Bearer <data>" is a JWT and must be sent as a Authorization header for all other rqeuests. If it's not present, endpoints should return 401 Unauthorized. If it's present and valid, the endpoints should function as normal.

