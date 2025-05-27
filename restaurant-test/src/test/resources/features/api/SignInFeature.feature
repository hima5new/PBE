@api
Feature: User Signin and Role Assignment

  As a registered user
  I want to sign in using various credentials
  So that I can verify login behavior and role assignment

  @smoke
  Scenario Outline: Login attempts with different credentials
    Given I have user credentials with email "<email>" and password "<password>"
    When I send a login request
    Then I should get a login response with status code <statusCode>
    And the login response should match user login schema

    Examples:
      | email                 | password       | statusCode |
      | olaf_frozen@gmail.com | Magicalfrost1@ | 200        |

  @regression
  Scenario Outline: Login attempts with different credentials
    Given I have user credentials with email "<email>" and password "<password>"
    When I send a login request
    Then I should get a login response with status code <statusCode>
    And the login response should match the expected message "<message>"

    Examples:
      | email                 | password                                     | statusCode | message                                                                        |
      |                       | Magicalfrost1@                               | 400        | Email is required for sign-in                                                  |
      | olaf_frozen@gmail.com |                                              | 400        | Password is required for sign-in                                               |
      | surya_teja@gmail      | Password123!                                 | 400        | Invalid email format. Please ensure it follows the format username@domain.com. |
      | olaf_frozen@gmail.com | Magicalfrost@                                | 401        | Invalid email or password.                                                     |
      | olaf_frozen@gmail.com | Magicalfrostezsxdrcftgvuhszerxxxxxghdrcfgvy@ | 401        | Invalid email or password.                                                     |


  @smoke
  Scenario Outline: Role assignment upon successful login
    Given I have user credentials with email "<email>" and password "<password>"
    When I send a login request
    Then I should get a login response with status code <statusCode>
    And the role in the response should be "<role>"
    And the login response should match user login schema

    Examples:
      | email                 | password        | statusCode | role      |
      | wanda@restaurant.com  | Password123!    | 200        | Waiter    |
      | elsa_frozen@gmail.com | Magicalfrost1@  | 200        | Customer  |