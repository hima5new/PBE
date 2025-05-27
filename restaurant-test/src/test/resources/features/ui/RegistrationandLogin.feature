@ui
Feature: User Registration and Login

  @regression
  Scenario Outline: Registration fails because of invalid inputs
    Given the user navigate to the registration page
    When the user fill in the registration form with the following inputs:
      | First Name  | Last Name  | Email               | Password   | Confirm Password  |
      | <FirstName> | <LastName> | <Email>             | <Password> | <ConfirmPassword> |
    And submit the registration form
    Then the user should see an error message "<ExpectedErrorMessage>"

    Examples:
      | FirstName | LastName | Email                   | Password     | ConfirmPassword | ExpectedErrorMessage                                     |
      | Anirwin   | Yaswa    | yaswa@gmail.com         | Anirwin@1234 | Anirwin@1234    | Registration failed. Please check your details.           |
      | Eesha     | Nair     | ezzeesha@com            | P4ssword!    | P4ssword!       | Invalid email address. Please ensure it follows the format: username@domain.com          |
      #| Eesha     | Nair     | eesha@email.com         | P4ssword!    | Abcdef234!      | Passwords do not match. Please try again.           |
      | Eesha     | Nair     | eesha@email.com         | p12345567    | p12345567       | ● At least one special character required                |
      |  ""         | Nair     | eesha@email.com         | P4ssword!    | P4ssword!      | First name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed.            |
      |Eeesha       | ""       | eesha@email.com         |  P4ssword!   | P4ssword!      | Last name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed.. |
      |Alexandrianna-Marie O'Connell-Johnson Smithworthington| Nair | eesha@email.com |P4ssword! |P4ssword!|First name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed.|
      |eeshu@12344 | rebba | eesha@email.com  | Password123! | Password123!  |First name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed.                             |

  @regression
  Scenario: Successful registration with valid inputs
    Given the user navigate to the registration page
    When the user fill in the registration form with valid inputs
      | First Name      | Last Name   | Email                     | Password   | Confirm Password   |
      | Eesha           | Rebbaa       | eeshaauto20@email.com      | Aa!12345   | Aa!12345           |
    And submit the registration form
    Then the user should see a success message "User registered successfully."
    And the user should be redirected to the login page

  @smoke
  Scenario Outline: Login with registered credentials
    Given the user navigate to the login page
    When the user log in with registered credentials
      | Email           | Password   |
      | <Email>         | <Password> |
    Then the user should be redirected to the user dashboard
    And should see a welcome message "<WelcomeMessage>"

    Examples:
      | Email                  | Password     | WelcomeMessage               |
      | yaswa@gmail.com    |   Anirwin@1234  | Logged in successfully       |
  @smoke
  Scenario: Registration fails when no inputs provided
    Given the user navigate to the registration page
    And submit the registration form
    Then the user should see an error message "First name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed."