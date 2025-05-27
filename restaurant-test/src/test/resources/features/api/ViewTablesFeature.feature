@api
Feature: Fetch Available Tables for Booking

  @smoke
  Scenario: Fetch available tables successfully with all inputs
    Given I am ready to fetch available tables
    When I send a request to fetch available tables with locationId "loc2", date "2025-06-15", time "10:30", and guests 4
    Then I should receive a 200 status code and the response has valid schema

  @smoke
  Scenario: Fetch available tables successfully without time parameter
    Given I am ready to fetch available tables
    When I send a request to fetch available tables with locationId "loc2", date "2025-06-15", and guests 4 without time
    Then I should receive a 200 status code and the response has valid schema

  @regression
  Scenario: Attempt to fetch tables with missing guests parameter
    Given I am ready to fetch available tables
    When I send a request to fetch available tables with missing parameters
      | locationId | loc2       |
      | date       | 2025-06-15 |
    Then I should receive a 400 status code and an error message "Required parameter 'guests' of type 'String' is missing"

  @regression
  Scenario: Attempt to fetch tables with missing locationId parameter
    Given I am ready to fetch available tables
    When I send a request to fetch available tables with missing parameters
      | date   | 2025-06-15 |
      | guests | 4          |
    Then I should receive a 400 status code and an error message "Required parameter 'locationId' of type 'String' is missing"

  @regression
  Scenario: Attempt to fetch tables with missing date parameter
    Given I am ready to fetch available tables
    When I send a request to fetch available tables with missing parameters
      | locationId | loc2 |
      | guests     | 4    |
    Then I should receive a 400 status code and an error message "Required parameter 'date' of type 'String' is missing"

  @regression
  Scenario: Attempt to fetch tables with missing all query parameters
    Given I am ready to fetch available tables
    When I send a request to fetch available tables without query parameters
    Then I should receive a 400 status code and an error message "Required parameter 'locationId' of type 'String' is missing"