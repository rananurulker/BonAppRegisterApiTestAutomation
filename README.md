# Test Automation Framework For Register Api Tests

This project is an API test automation framework developed using **Java**, **Rest-Assured**, and **Cucumber**. It follows the Behavior-Driven Development (BDD) approach and uses **Maven** for dependency management and build automation.

## Project Structure

The project has the following structure:

```
src
└── test
    └── java
        ├── runners
        │   └── runner
        ├── stepDefinitions
        │   ├── RegistrationApiConfirmationTestsSteps
        │   └── RegistrationApiNegativeTestsSteps
        └── utilities
            └── ConfigReader
    └── resources
        └── features.API
            ├── RegisterApiConfirmation.feature
            └── RegisterApiNegativeTests.feature

configuration.properties
pom.xml
README.md
```

### Key Components

1. **Runners**: Contains the Cucumber runner class to execute the tests.
2. **Step Definitions**: Implements the steps defined in the feature files using Rest-Assured.
3. **Utilities**: Includes utility classes such as `ConfigReader` for reading configuration properties.
4. **Feature Files**: Contains BDD feature files that define the test scenarios in Gherkin syntax.
5. **configuration.properties**: Contains all required variables like base URLs, mail authorization, mail domain, etc.
6. **pom.xml**: Maven configuration file for managing dependencies and build lifecycle.

## Prerequisites

Make sure you have the following installed on your system:

- Java (JDK 8 or higher)
- Maven (latest version)
- An IDE like IntelliJ IDEA or Eclipse

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd BonAppRegisterApiTestAutomation
   ```
3. Install the required dependencies:
   ```bash
   mvn clean install
   ```

## Running the Tests

### Using Maven

To run the tests, use the following Maven command:

```bash
mvn test
```

After the execution, a Cucumber HTML report will be generated in the `target` directory.

### Using an IDE

Alternatively, you can run the tests through an IDE:

1. Open the project in your IDE (e.g., IntelliJ IDEA or Eclipse).
2. Navigate to the `runner` class in the `runners` package.
3. Right-click the class and select **Run** to execute the tests.

### Viewing the Report

1. Navigate to the `target` directory.
2. Open the generated Cucumber HTML report (e.g., `cucumber-report.html`) in a web browser to view the test results.

## Dependencies

All required dependencies are specified in the `pom.xml` file. Some key dependencies include:

- **Rest-Assured**: For API testing.
- **Cucumber**: For BDD framework support.
- **JUnit**: For test execution.

## Configuration

All required variables like base URLs, mail authorization, mail domain, and other environment-specific configurations are included in the `configuration.properties` file. There is no need to update this file manually.
