# Book API Backend

This project serves as the backend for the Book API project, built using Spring Boot. It incorporates JWT authentication for secure user access and includes Swagger for API documentation.

## Table of Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)


## Getting Started

These instructions will guide you on how to set up and run the project on your local machine.

## Prerequisites

List any prerequisites or dependencies that need to be installed before running the project. For example:

- Java Development Kit (JDK) version 11 or higher
- Maven
- MySQL

## Installation

Provide step-by-step instructions on how to install and configure the project. Include commands or links to additional resources if necessary. For example:

1. Clone the repository:

```bash
 git clone https://github.com/indrajohn/book-api.git
```

2. Navigate to the project directory:

```bash
 cd book-api
```

3. Build the project using Maven:
```bash
 mvn clean install
```
4. Configure the database connection in the `application.properties` file.

## Usage

Explain how to run the project. Include any command-line options or parameters that may be required. For example:

1. Run the project using Maven:
```bash
mvn spring-boot:run
```

2. Open a web browser and access the application at `http://localhost:8080`.


## API Documentation

The API documentation for this project is generated using Swagger. Swagger provides an interactive interface to explore and test the available endpoints. Follow the steps below to access and use the API documentation:

1. Ensure that the project is running locally. Refer to the Usage section for instructions on how to run the project.

2. Open a web browser and access the Swagger UI page at http://localhost:8080/swagger-ui/.

3. The Swagger UI will display a list of available endpoints along with their request and response schemas.

4. Some endpoints might require authentication using JWT (JSON Web Token). To access these endpoints, you need to obtain a JWT token.

5. Locate the login endpoint in the Swagger UI. Typically, it will be /api/auth/sign-in. Click on the endpoint to expand it.

6. In the login endpoint section, provide the required credentials (e.g., username and password) in the request body.

7. Click on the "Try it out" button to send the login request.

8. If the provided credentials are valid, the API will respond with a JWT token in the response body.

9. Copy the JWT token from the response.

10. To authenticate subsequent requests, add the JWT token to the request headers. Typically, you need to include an Authorization header with the value Bearer <token>, where <token> is the JWT token you obtained.

11. Now you can explore and test other endpoints that require authentication. Click on an endpoint to expand it, provide the necessary request parameters, and click on the "Try it out" button to send the request.

12. Review the response data and status codes returned by the API to understand the expected behavior.
