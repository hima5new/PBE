@api
Feature: Retrieve list of popular dishes

  @smoke
  Scenario: Successfully retrieve the list of popular dishes
    Given the popular dishes service is available
    When the user sends a GET request to retrieve popular dishes
    Then the popular dish response status code should be 200
    And the response should contain a list of popular dishes
    And every dish in the response should have the popular attribute set to true
    And the response should match the popular dishes schema