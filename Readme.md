# README

## Project Overview
This project is a **Java-based API testing framework** based on **Java 11** and  **Gradle**. It leverages **TestNG** for test execution and **RestAssured** for API interactions. The framework integrates **Allure** for reporting and follows modular design principles to ensure scalability and maintainability.

## Features
- **API Testing**: Supports CRUD operations and validations for RESTful APIs.
- **Data-Driven Testing**: Utilizes TestNG's `@DataProvider` for parameterized tests.
- **Allure Reporting**: Provides detailed test execution reports with steps, descriptions, and environment details.
- **Custom Assertions**: Includes reusable assertion methods for validating API responses.
- **Environment Configuration**: Centralized configuration management using `TestConfig`.

## Prerequisites
- **Java**: Version 11 or higher.
- **Gradle**: Version 7.0 or higher.
- **Allure**: Installed locally for generating reports.
- **IntelliJ IDEA**: Recommended IDE for development.

## Project Structure
```
src/
├── main/
│   └── java/ (Base code)
│       ├── utils/ (Utility classes for API requests and assertions)
│       ├── dto/ (Data Transfer Objects for requests and responses)
│       ├── config/ (Configuration classes)
│       └── enums/ (Enums for constants like roles or editors)
├── test/
│   └── java/
│       ├── tests/ (Test classes)
│       ├── assertions/ (Assertion Helper)

build/ (Generated build artifacts)
.idea/ (IDE-specific files)
```

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. Install dependencies:
   ```bash
   ./gradlew clean build
   ```

3. Configure the environment:
    - Update `TestConfig` with the appropriate `baseUrl`, `suiteName`, and `threadCount`.

4. Run tests: (to change forks apply -DmaxParallelForks=N, default is 3)
   ```bash
   ./gradlew apiTests 
   ```

5. Generate Allure reports:
   ```bash
   ./gradlew allureServe
   ```

## Key Components
### `BaseTest`
- Sets up the base URI for API requests.
- Configures Allure environment details.

### `PlayerClient`
- Contains methods for interacting with the API endpoints (e.g., create, update, delete users).
- Logs requests and responses for debugging and reporting.

### `PlayerRequestUtils`
- Utility class for generating random test data for API requests.

### `PlayerAssertions`
- Contains reusable assertion methods for validating API responses.
