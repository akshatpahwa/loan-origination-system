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

- **Java 17** or higher
- **Maven 3.8+**

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
