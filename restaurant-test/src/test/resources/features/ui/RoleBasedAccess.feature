@ui
Feature: Role-Based User Access and Assignment
  This feature validates automatic user role assignment based on predefined data such as email lists for Waiters and Customers.

  @regression
  Scenario: Role Assignment for Registered Waiter
    Given The email "captian@restaurant.com" exists in the Waiter's email list
    And The user is on the registration page
    When The user registers with the email "captian@restaurant.com" and provides valid inputs
    And The user logs in with the waiter email and password
    And The user navigates to the profile page
    Then The user is assigned the role "(Waiter)"
    And The role "(Waiter)" is displayed in the user's profile

  @invalid
  Scenario: Role Assignment for Customer Role
    Given The email "vaishnavi_srividya2603@gmail.com" does NOT exist in the Waiter's email list
    And The user is on the registration page
    When The user registers with the email "vaishnavi_srividya2603@gmail.com" and provides valid inputs
    And The user logs in with the customer email and password
    And The user navigates to the profile page
    Then The user is assigned the role "(Customer)"
    And The role "(Customer)" is displayed in the user's profile

  @regression
  Scenario: Visitor Role Usage
    Given The user has not registered or logged into the application
    When The user accesses visitor-specific features
    Then The user is automatically assigned the role "Visitor"
    And Visitor features are accessible without requiring registration or login

  @regression
  Scenario: Validate Role Display in Profile
    Given The user has an account with the email "vaishnavi_srividya2603@gmail.com"
    And The user logs in with the email "vaishnavi_srividya2603@gmail.com"
    When The user navigates to the profile page
    Then The role "(Customer)" is displayed in the profile

  @invalidUnCheck
  Scenario: Priority Role Assignment for Waiter
    Given The email "peter.parker@restaurant.com" exists in both Waiter and Customer lists
    And The user is on the registration page
    When The user registers with the email "peter.parker@restaurant.com" and logs in
    Then The user is assigned the role "(Waiter)" because it has higher priority over "(Customer)"
    And The role "(Customer)" is displayed in the profile

  @smoke
  Scenario: User Role Assignment Validation for Invalid Emails
    Given The user is on the registration page
    When The user registers with the invalid email "invalid@com"
    Then The system shows a validation error message "Invalid email address. Please ensure it follows the format: username@domain.com"