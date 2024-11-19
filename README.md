# Receipt Processing Service

## Project Description

The Receipt Processing Service is a Java Spring Boot application that processes receipts and calculates points based on the total amount. This project includes two main functionalities:
1. Processing a receipt to generate a unique receipt ID and calculate points.
2. Retrieving points for a given receipt ID.

## Features

- **Process Receipt**: Accepts a receipt in JSON format, processes it, calculates points, and returns a unique receipt ID.
- **Get Points**: Fetches and returns the points for a given receipt ID.

## Technologies Used

- Java
- Spring Boot
- SLF4J (Simple Logging Facade for Java)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed
- Maven installed
- An IDE such as Eclipse (optional)

## Setup Instructions

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-username/receipt-processing-service.git
   ```

2. **Navigate to the project directory:**
   ```sh
   cd receipt-processing-service
   ```

3. **Import the project into your IDE (Eclipse):**
   - Open Eclipse.
   - Go to `File -> Import -> Maven -> Existing Maven Projects`.
   - Select the cloned repository directory.
   - Click `Finish`.

4. **Build the project:**
   ```sh
   mvn clean install
   ```

5. **Run the application:**
   - In Eclipse, right-click on the project in the Project Explorer.
   - Select `Run As -> Spring Boot App`.

## Usage

### Endpoints

- **Process Receipt**

  - **URL:** `POST /receipts/process`
  - **Description:** Processes a receipt and calculates points.
  - **Request Body:** JSON object representing the receipt.
  - **Response:** JSON object containing the generated receipt ID.

## Code Overview

### `ReceiptController.java`

This class defines the REST API endpoints for processing receipts and retrieving points. It uses the `ReceiptService` to handle business logic.

### `ReceiptService.java`

This class contains the business logic for processing receipts and calculating points. It maintains a map of receipt IDs to points.

## Logging

The application uses SLF4J for logging. Logs include details about receipt processing, point calculation, and error handling.

## Error Handling

The application handles the following errors:
- **Null Pointer Exceptions**: When receipt data or receipt ID is `null`.
- **Illegal Argument Exceptions**: When a receipt ID is not found or when the receipt total value is invalid.

## Contact

If you have any questions or feedback, please reach out to:
- **Email**: Sreelakshmivallabhaneni@gmail.com
