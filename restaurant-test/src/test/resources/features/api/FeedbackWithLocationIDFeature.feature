@api
Feature: Retrieve Feedback for Restaurant Locations

  @smoke
  Scenario: Retrieve feedback for valid location id with type "cuisine" sorted by "best"
    Given I have location id "loc1" with feedback type "cuisine" and sorting "best"
    When I send a request to retrieve feedback
    Then I should receive a 200 status code and feedback details
    And the response should match the feedback schema

  @smoke
  Scenario: Retrieve feedback for valid location id with type "service" sorted by "newest"
    Given I have location id "loc2" with feedback type "service" and sorting "newest"
    When I send a request to retrieve feedback
    Then I should receive a 200 status code and feedback details
    And the response should match the feedback schema

  @regression
  Scenario: Return 404 for invalid location id
    Given I have location id "loc9" with feedback type "cuisine" and sorting "best"
    When I send a request to retrieve feedback
    Then I should receive a 404 status code for invalid feedback retrieval and an error message "No such location found."

  @regression
  Scenario: Return 400 for valid location but invalid feedback type
    Given I have location id "loc1" with feedback type "cui" and sorting "newest"
    When I send a request to retrieve feedback
    Then I should receive a 400 status code for invalid feedback type retrieval and an error message "Invalid feedback type. Allowed values: cuisine, service"