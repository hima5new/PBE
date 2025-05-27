@api
Feature: Dishes API Testing
  As a user
  I want to validate filtering, retrieving dishes, and handling edge cases
  So that I can ensure the API handles diverse scenarios accurately.

  Background:
    Given the user is signed in with valid credentials
  @smoke
  Scenario Outline: Verify filtering dishes by dish type
    Given the user requests dishes of type "<dishType>"
    Then the response status should be <statusCode>
    And the response contains "<expectedOutcome>"

    Examples:
      | dishType       | statusCode | expectedOutcome |
      | Appetizers     | 200        | a list of dishes |
      | Main Courses   | 200        | a list of dishes |
      | Desserts       | 200        | a list of dishes |
      | InvalidType    | 400        | an error message 'Invalid dish type(s). Allowed values: Appetizers, Main Courses, Desserts' |
  @smoke
  Scenario Outline: Verify retrieving a dish by ID
    Given the user requests a dish with ID <id>
    Then the response status should be <statusCode>
    And the response returns "<expectedOutcome>"

    Examples:
      | id   | statusCode | expectedOutcome                   |
      | 1    | 200        | dish details with correct attributes |
      | 1234 | 404        | an error message 'Dish not found'   |
  @smoke,@regression
  Scenario Outline: Verify errors for invalid sorting or query parameters
    Given the user requests dishes with invalid sort column "<invalidSort>" for type "<dishType>"
    Then the response status should be <statusCode>
    And the error message should be "<expectedMessage>"

    Examples:
      | invalidSort      | dishType      | statusCode | expectedMessage                                                  |
      | invalid          | Appetizers    | 400        | Invalid sort parameter                                           |
      | price,xyz        | Appetizers    | 400       | Invalid sort parameter                                  |
      | popularity,asc   |               | 400        | dishType is a required parameter       |
      |                  |               | 400        |  dishType is a required parameter      |
@regression
  Scenario Outline: Verify retrieving dishes with missing or incomplete endpoints
    Given the user requests dishes with a missing ID
    Then the response status should be <statusCode>
    And the error message should be "<expectedMessage>"

    Examples:
      | statusCode | expectedMessage                  |
      | 503        | Required ID parameter is missing |
      | 503        | Missing query parameters         |
      | 503        | Invalid endpoint                 |
      | 503        | Dish not found                   |
@regression
  Scenario Outline: Verify retrieving dishes by dish type with sorting
    Given the user requests dishes sorted by "<sortColumn>" in "<sortOrder>" order for type "<dishType>"
    Then the response status should be <statusCode>
    And the response contains dishes sorted by "<sortColumn>"

    Examples:
      | dishType     | sortColumn  | sortOrder | statusCode |
      | Appetizers   | popularity  | asc       | 200        |
      | Main Courses | price       | desc      | 200        |
      | Desserts     | popularity  | desc      | 200        |
