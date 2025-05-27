@api
Feature: View Restaurant Location Details

  @smoke
  Scenario: Validating locations response
    Given the restaurant locations service is available
    When the user sends a GET request to retrieve locations
    Then the locations response status code should be 200
    And the response should contain a list of locations
    And the response should match the expected locations schema