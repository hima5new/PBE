@api
Feature: User Registration Testing

  @smoke
  Scenario Outline: Validate valid user registration
    Given a user with firstName "<firstName>", lastName "<lastName>", email "<email>", and password "<password>"
    When I submit the registration request
    Then I should receive registration response with status code <expectedStatusCode> and message "<expectedMessage>"

    Examples:
      | firstName  | lastName  | email                                 | password     | expectedStatusCode | expectedMessage               |
      | Vasudev    | Yadav     | vasudev_yadav12345678@gmail.com       | Password123! | 201                | User registered successfully. |
      | Vaishnavi' | SriVidya  | vaishnavi_sssrriivviiddyya@gmail.com  | Vaishnavi26@ | 201                | User registered successfully. |
      | Vaishnavi  | SriVidya' | vaishnavi_sssrriivviiddyya@gmail.com  | Vaishnavi26@ | 201                | User registered successfully. |
      | Vaishnavi- | SriVidya  | vaishnavi_sssrriivviiddyya@gmail.com  | Vaishnavi26@ | 201                | User registered successfully. |
      | Vaishnavi  | SriVidya- | vaishnavi_sssrriivviiddyya@gmail.com  | Vaishnavi26@ | 201                | User registered successfully. |
      | Vaishnavi  | SriVidya  | vaishnavi_sssrriivviiddyya-@gmail.com | Vaishnavi26@ | 201                | User registered successfully. |

  @regression
  Scenario Outline: Validate invalid user registration scenarios
    Given a user with firstName "<firstName>", lastName "<lastName>", email "<email>", and password "<password>"
    When I submit the registration request
    Then I should receive registration response with status code <expectedStatusCode> and message "<expectedMessage>"

    Examples:
      | firstName  | lastName  | email                             | password                  | expectedStatusCode | expectedMessage                                                                                                                                                             |
      | Olaf       | Frozen    | olaf_frozen@gmail.com             | Magicalfrost1@            | 409                | An user already exists with this email                                                                                                                                      |
      | Vaishnavi  | SriVidya  |                                   | Vaishnavi26@              | 400                | Invalid registration request: Email is required                                                                                                                             |
      |            | SriVidya  | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: First name is required                                                                                                                        |
      | Vaishnavi  |           | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: Last name is required                                                                                                                         |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  |                           | 400                | Invalid registration request: Password is required                                                                                                                          |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603gmail.com   | Vaishnavi26@              | 400                | Invalid registration request: Invalid email format. Please ensure it follows the format username@domain.com.                                                                |
      | Vaishnavi1 | SriVidya  | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.                                             |
      | Vaishnavi  | SriVidya1 | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.                                              |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@.com       | Vaishnavi26@              | 400                | Invalid registration request: Invalid email format. Please ensure it follows the format username@domain.com.                                                                |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail..com | Vaishnavi26@              | 400                | Invalid registration request: Invalid email format. Please ensure it follows the format username@domain.com.                                                                |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  | Abcdefghijklmnopqrstuvw$1 | 400                | Invalid registration request: Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. |
      | Vaishnavi$ | SriVidya  | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: First name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.                                             |
      | Vaishnavi  | SriVidya$ | vaishnavi_srividya2603@gmail.com  | Vaishnavi26@              | 400                | Invalid registration request: Last name must be up to 50 characters. Only Latin letters, hyphens, and apostrophes are allowed.                                              |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  | VAISHNAVI26@              | 400                | Invalid registration request: Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  | Vaishnavi                 | 400                | Invalid registration request: Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  | Vai1@                     | 400                | Invalid registration request: Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. |
      | Vaishnavi  | SriVidya  | vaishnavi_srividya2603@gmail.com  | vaishnavi_srividya1@      | 400                | Invalid registration request: Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character. |
      | Ayan       | Mukherjee | -@gmail.com                       | Password123!              | 400                | Invalid registration request: Invalid email format. Please ensure it follows the format username@domain.com.                                                                |
      | Ayan       | Mukherjee | .@gmail.com                       | Password123!              | 400                | Invalid registration request: Invalid email format. Please ensure it follows the format username@domain.com.                                                                |