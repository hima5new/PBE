@api
Feature: Fetch Reservations List

  @smoke
  Scenario: Fetch reservations successfully and validate reservation ids
    Given I log in with valid username "olaf_frozen@gmail.com" and password "Magicalfrost1@"
    When I send a request to fetch reservations with the token
    Then I should receive reservation response with 200 status code and valid details
    And the response should match the reservations schema

  @regression
  Scenario: Fetch reservations with null token and validate error response
    Given the system is setup to handle requests with null tokens
    When I send a request to fetch reservations with a null token
    Then I should receive reservation response with  401 status code
    And the reservation response should contain an appropriate error message "Please sign in to continue"