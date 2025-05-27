@api
Feature: Verify special dish IDs for a restaurant location

  @smoke
  Scenario: Validating special dishes response
    Given the user provides the restaurant location id "loc1"
    When I send a GET request to retrieve special dishes for the location
    Then the special dishes response status code should be 200
    And the response should match the special dishes schema

  @regression
  Scenario: Attempting to retrieve special dish IDs with an invalid restaurant location id
    Given the user provides the restaurant location id "loc9"
    When I send a GET request to retrieve special dishes for the location
    Then the special dishes response status code should be 404
    And the special dishes response should contain an error message "No valid Location found"