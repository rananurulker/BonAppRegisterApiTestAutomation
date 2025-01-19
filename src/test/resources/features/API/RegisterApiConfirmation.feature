@PositiveFlow
Feature: BonApp API Registration and Management
  This feature tests the BonApp API endpoints for user registration, email confirmation, and password management.

  Background:
    Given the base URL is set as BonApp

  Scenario: User Registration
    Given I generate a random email address
    When I send a POST request to "/api/account/register" with the valid data
    Then the response status code should be 200

  Scenario: Confirm email by using userID and code
    Given I receive the emails from mailosaur server and extract userID and code
    When I send a POST request to "/api/account/confirmEmail" with query parameters userID and code
    Then the response status code should be 200

  Scenario: Resend Confirmation Email
    When I send a POST request to "/api/account/sendConfirmationEmail" for resending confirmation email
    Then the response status code should be 200


  Scenario: Forgot Password Request
    When I send a POST request to "/api/account/changePasswordRequest" for password change
    Then the response status code should be 200

 Scenario: Change Password
   When I send a POST request to "/api/account/changePassword" with the reset code
   Then the response status code should be 200