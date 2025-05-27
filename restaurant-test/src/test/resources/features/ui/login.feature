@ui
Feature: Login Functionality
  As a user,
  I want to log in using my email and password
  So that I can access my dashboard and sessions are consistent.

  @regression
  Scenario Outline: Successful login with valid credentials
    Given The user is on the login page
    When The user enters email "<email>" and password "<password>"
    And The user clicks on the "Sign In" button
    Then A success message "Logged in successfully" should display

    Examples:
      | email               | password       |
      | yaswa@gmail.com     | Anirwin@1234   |
  @invalid
  Scenario: Successful logout from the home page
    Given The user is logged in
    When The user clicks the "Logout" button
    Then The user should be redirected to the login page


  @regression
  Scenario Outline: Failed login with invalid credentials
    Given The user is on the login page
    When The user enters email "<email>" and password "<password>"
    And The user clicks on the "Sign In" button
    Then An error message "<error_message>" should display

    Examples:
      | email              | password         |error_message                                          |
      | user@example.com   | Wrongpass123!    |  Invalid email or password.      |
      |  yaswa@gmail.com   |  Wrongpass123!   |  Invalid email or password.        |
  @regression
  Scenario Outline: Login attempt with missing input fields
    Given The user is on the login page
    When The user enters emails "<email>" and password "<password>"
    And The user clicks on the "Sign In" button
    Then An error message "<error_message>" should display

    Examples:
      | email            | password       | error_message                                                       |
      #|                  |                | Email address is required. Please enter your email to continue      |
      | user@example.com |                | Password is required for sign-in       |
      |                  | password123!   | Email is required for sign-in      |

  @smoke
  Scenario Outline: Login attempt with invalid email format
    Given The user is on the login page
    When The user enters email "<email>" and password "<password>"
    And The user clicks on the "Sign In" button
    Then An error message "Invalid email format. Please ensure it follows the format username@domain.com." should display
    Examples:
      | email         | password      |
      | millie@com    | Password123!  |

  @smoke
  Scenario Outline: Login persistence across sessions
    Given The user is on the login page
    When The user enters email "<email>" and password "<password>"
    And The user clicks on the "Sign In" button
    Then A success message "Logged in successfully" should display
    When The user closes and reopens the browser
    Then The user should remain logged in

    Examples:
      | email               | password      |
      | millie@auto.com   | Password123!  |

  @regression
  Scenario: Verify page elements are displayed
    Given The user is on the login page
    Then The login page should display the email field
    And The login page should display the password field
    And The login page should display the "Sign In" button
