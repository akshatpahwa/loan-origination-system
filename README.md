# Loan Origination System

The Loan Origination System is a Spring Boot application designed to manage the lifecycle of loan applications. It provides RESTful APIs for submitting loan applications, retrieving loan statuses, and managing loans with pagination and filtering capabilities.

## Features

- **Submit Loan Applications**: Create new loan applications with customer details and loan information.
- **Loan Status Count**: Retrieve the count of loans grouped by their statuses.
- **Pagination and Filtering**: Fetch loans based on their status with pagination support.
- **Agent Assignment and Review**: Assign agents to loan applications and submit reviews.
- **Top Customers API**: Retrieve the top customers based on the total loan amount.
- **Test Coverage**: Unit tests for controllers and services to ensure reliability.

## Technologies Used

- **Java 17**: Core programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Data JPA**: For database interactions.
- **H2 Database**: In-memory database for development and testing.
- **JUnit 5**: For unit testing.
- **Mockito**: For mocking dependencies in tests.
- **Maven**: Build tool for dependency management.

## Prerequisites

- **Java 17** or higher (project is configured for Java 21 in `pom.xml`)
- **Maven 3.8+`

## Getting Started

### Clone the Repository
```bash
git clone <repository-url>
cd loan-origination-system
```

### Build the Project
```bash
./mvnw clean install
```

### Run the Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### Run Tests
```bash
./mvnw test
```

## API Endpoints

### Submit Loan Application
- **POST** `/api/v1/loans`
- **Request Body**:
```json
{
  "customerName": "John Doe",
  "customerPhone": "1234567890",
  "loanAmount": 50000,
  "loanType": "HOME"
}
```
- **Response**:
```json
{
  "loanId": "<UUID>",
  "customerName": "John Doe",
  "loanAmount": 50000,
  "loanType": "HOME",
  "status": "APPLIED"
}
```

### Get Loan Status Count
- **GET** `/api/v1/loans/status-count`
- **Response**:
```json
{
  "APPROVED_BY_SYSTEM": 3,
  "UNDER_REVIEW": 2
}
```

### Get Loans by Status with Pagination
- **GET** `/api/v1/loans`
- **Query Parameters**:
  - `status`: Loan status (e.g., `APPLIED`)
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 10)
- **Response**:
```json
{
  "content": [
    {
      "loanId": "<UUID>",
      "customerName": "John Doe",
      "loanAmount": 50000,
      "status": "APPLIED"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1
}
```

### Agent Assignment and Review
- **POST** `/api/v1/agents/assign`
- **Request Body**:
```json
{
  "agentId": "<UUID>",
  "loanId": "<UUID>"
}
```
- **Response**:
```json
{
  "message": "Agent assigned successfully",
  "agentId": "<UUID>",
  "loanId": "<UUID>"
}
```

- **POST** `/api/v1/agents/review`
- **Request Body**:
```json
{
  "agentId": "<UUID>",
  "loanId": "<UUID>",
  "review": "Loan application reviewed successfully"
}
```
- **Response**:
```json
{
  "message": "Review submitted successfully",
  "agentId": "<UUID>",
  "loanId": "<UUID>"
}
```

### Top Customers API
- **GET** `/api/v1/customers/top`
- **Query Parameters**:
  - `count`: Number of top customers to retrieve (default: 5)
- **Response**:
```json
[
  {
    "customerId": "<UUID>",
    "customerName": "John Doe",
    "totalLoanAmount": 150000
  },
  {
    "customerId": "<UUID>",
    "customerName": "Jane Smith",
    "totalLoanAmount": 120000
  }
]
```

## Folder Structure

- `src/main/java`: Application source code.
- `src/main/resources`: Configuration files and templates.
- `src/test/java`: Unit tests.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Run locally with PostgreSQL

This project uses H2 by default for development and testing. The instructions below show how to run the application locally using PostgreSQL — either via Docker (recommended) or a locally installed PostgreSQL instance.

### Prerequisites

- Java 17 or higher (project is configured for Java 21 in `pom.xml`)
- Maven 3.8+ (or use the project's Maven wrapper: `./mvnw`)
- Docker (optional, but recommended for running PostgreSQL locally)

### 1) Start PostgreSQL (Docker)

Quick Docker run:

```bash
docker run --name postgres-local \
  -e POSTGRES_USER=appuser \
  -e POSTGRES_PASSWORD=apppass \
  -e POSTGRES_DB=appdb \
  -p 5432:5432 \
  -d postgres:15
```

Or use a `docker-compose.yml` (recommended if you want a reproducible setup):

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: apppass
      POSTGRES_DB: appdb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```

Run with:

```bash
docker compose up -d
```

### 2) Configure the application

Create or update `src/main/resources/application-local.properties` (or modify `src/main/resources/application.properties`) with your DB settings:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/appdb
spring.datasource.username=appuser
spring.datasource.password=apppass
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Alternatively you can pass these as environment variables when running the app:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_PROFILES_ACTIVE=local` (if you use a `local` profile file)

### 3) Ensure PostgreSQL JDBC driver is available

Check `pom.xml` for the PostgreSQL driver dependency. If it's not present, add this dependency and rebuild:

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.6.0</version>
</dependency>
```

### 4) Run the application pointing to the `local` profile (or with properties/env vars set)

Using the Maven wrapper and `local` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Or build the jar and run it directly:

```bash
./mvnw clean package
java -jar target/*-SNAPSHOT.jar --spring.profiles.active=local
```

(Replace `*-SNAPSHOT.jar` with the actual artifact name created by the build.)

### 5) Verify the database connection & troubleshooting

- Check the PostgreSQL container logs:

```bash
docker logs -f postgres-local
```

- Test DB connectivity from the host (requires `psql` client):

```bash
psql postgresql://appuser:apppass@localhost:5432/appdb
```

### Common issues

- Port 5432 already in use: change the host side port mapping in the Docker command or `docker-compose.yml`.
- Wrong credentials or DB name: confirm the `spring.datasource.*` settings match the DB you created.
- Missing JDBC driver: add the PostgreSQL dependency to `pom.xml` and rebuild.
- Hibernate DDL behavior: `spring.jpa.hibernate.ddl-auto=update` will attempt to alter the schema. For production, prefer `validate` or manage schema migrations with Flyway/Liquibase.

### Notes

- Replace `appdb`, `appuser`, `apppass`, and the artifact name with values appropriate for your environment.
- You can also use a local Postgres installation instead of Docker — ensure the `spring.datasource.url` points to the correct host/port and that the DB/user exist.
