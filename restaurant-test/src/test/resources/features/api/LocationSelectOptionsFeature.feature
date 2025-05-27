@api
Feature: Location Select Options

  @smoke
  Scenario: Verify fetching a list of short location details is successful
    Given the location select options API is available
    When I send a GET request to fetch short location details
    Then I should receive a "200 OK" status code
    And the response should contain a list of locations with addresses and ids
    And the location options response should match the location schema