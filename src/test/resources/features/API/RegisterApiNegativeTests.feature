@NegativeRegister
Feature: BonApp Registration API Negative Tests
  To ensure the registration API properly handles invalid requests,
  the system should return appropriate error messages and HTTP status codes.

  Background:
    Given the base URL is set as BonApp
    And the API endpoint is register

  Scenario: Attempt to register with an already registered email
    When I send a POST request with a registered email
    Then the response status code should be 400
    And the response should contain the error message "User with same email already exists"

  Scenario: Attempt to register with a weak password
    When I send a POST request with a weak password
    Then the response status code should be 400
    And the response should contain the error message "Password validation failed"

  Scenario: Attempt to register with an invalid email format
    When I send a POST request with an invalid email format
    Then the response status code should be 400
    And the response should contain the error message "'Email' is not a valid email address."

  Scenario: Attempt to register with a missing confirmation URL
    When I send a POST request with a missing confirmation URL
    Then the response status code should be 400
    And the response should contain the error message "'Email Confirmation Url' must not be empty."

  Scenario: Attempt to register with missing required fields (email)
    When I send a POST request with a missing email field
    Then the response status code should be 400

  Scenario: Attempt to register with missing required fields (password)
    When I send a POST request with a missing password field
    Then the response status code should be 400
