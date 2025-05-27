@api
Feature: Feedback API Scenarios
  As a customer
  I want to submit and view feedback based on reservation status
  So that I can share my dining experience

  Background:
    Given the user is authenticated

  Scenario Outline: Submit feedback when reservation status is <status>
    Given reservation with ID "<reservationId>" is in status "<status>"
    When the user submits feedback with cuisine rating "<cuisineRating>" and service rating "<serviceRating>"
    Then the response status code should be 201
    And the message should be "Feedback has been submitted successfully"

    Examples:
      | reservationId | status      | cuisineRating | serviceRating |
      | 2b33dde7      | In Progress | 5             | 4             |
      | 6f47e2e6      | Finished    | 3             | 4             |
@smoke
  Scenario: Submit feedback without ratings
    When the user submits feedback with missing ratings for reservationId "2b33dde7"
    Then the response status code should be 400
    And the message should be "rating is required."
@smoke
  Scenario: Submit feedback without reservation ID
    When the user submits feedback without reservation ID
    Then the response status code should be 400
    And the message should be "Reservation ID is required"

  Scenario: View feedback when reservation is In Progress
    When the user attempts to view feedback for reservation "2b33dde7"
    Then the response status code should be 400
    And the message should be "Feedback can only be viewed for reservations that are finished'"

  Scenario: Update feedback for finished reservation
    When the user updates feedback for reservationId "6f47e2e6"
    Then the response status code should be 201
    And the message should be "Feedback has been submitted successfully"
