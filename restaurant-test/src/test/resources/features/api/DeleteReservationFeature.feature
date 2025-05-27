@api
Feature: Deleting a reservation

  @smoke
  Scenario: Attempt to cancel a reservation that does not belong to the logged-in user
    Given I log in with email "olaf_frozen@gmail.com" and password "Magicalfrost1@"
    When I send a DELETE request to cancel a reservation with id "3809e2ba"
    Then I should receive a response with status code 200 and message "Reservation cancelled successfully"

  @regression
  Scenario: Attempt to cancel a reservation that does not belong to the logged-in user
    Given I log in with email "elsa_frozen@gmail.com" and password "Magicalfrost1@"
    When I send a DELETE request to cancel a reservation with id "adda0ed8"
    Then I should receive a response with status code 401 and message "Not authorized to cancel this reservation"

  @regression
  Scenario: Attempt to cancel a reservation without being logged in
    Given I am not logged in
    When I try to delete a reservation with id "adda0ed8" as a non-logged-in user
    Then I should receive a response with status code 401 and message "Please sign in to continue"