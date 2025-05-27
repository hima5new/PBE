@ui
Feature: Table Filtering and Booking
  As a logged-in user,
  I want to filter and book tables
  So that I can efficiently find and reserve the best table for my needs.

  Background:
    Given the user is logged in
    And the user navigates to Book a Table page

  @valid
  Scenario Outline: Verify filtering tables by location
    Given tables exist in multiple locations
    When the user selects a location filter "<location>"
    Then only tables from the selected location "<location>" are displayed

    Examples:
      | location      |
      |    123 Main Street, Downtown, New York, NY 10001  |
      |456 Ocean Avenue, Beachside, Miami, FL 33139   |
      | 789 Highland Drive, Westside, Los Angeles, CA 90046      |

  @guestCountFilter
  Scenario Outline: Verify filtering tables by guest count
    Given the user is on the "Book a Table" page
    When the user selects guest count "<guest_count>"
    Then only tables that accommodate the specified guest count "<guest_count>" are displayed

    Examples:
      | guest_count |
      | 2           |
      | 4           |
      | 6           |

  @valid
  Scenario Outline: Verify filtering tables by available timeslot
    Given tables are available in different timeslots
    When the user selects a specific timeslot "<timeslot>"
    Then only available tables at the selected timeslot "<timeslot>" are displayed

    Examples:
      | timeslot  |
      | 10:30 - 12:00  |
      |  19:15 - 20:45 |
      | 21:00 - 22:30  |

  @reservationCards
  Scenario Outline: Verify reservation cards appear after filtering and selecting a timeslot
    Given the user is on the "Book a Table" page
    When the user applies filters for location "<location>", guest count "<guest_count>", timeslot "<timeslot>", and date "<date>"
    And selects a table for "<guest_count>" capacity and clicks an available timeslot
    Then reservation cards with guest count "<guest_count>" and a "Make a Reservation" button are displayed

    Examples:
      | location                                             | guest_count   | timeslot   | date      |
      | 123 Main Street, Downtown, New York, NY 10001        | 4             | 10:30 - 12:00  | 22-05-2025          |
      |  789 Highland Drive, Westside, Los Angeles, CA 90046 | 2             | 10:30 - 12:00   |  22-05-2025       |
      | 456 Ocean Avenue, Beachside, Miami, FL 33139         | 6             | 10:30 - 12:00    |  22-05-2025      |

  @navigateToReservation
  Scenario: Verify page navigation and validation message after selecting a timeslot
    Given the user is on the table booking page
    When the user applies filters for location "123 Main Street, Downtown, New York, NY 10001", guest count "4", and timeslot "12:00 PM"
    And selects a table and clicks on an available timeslot
    Then a validation message "Reservation confirmed! See you soon." is displayed
    And the user is redirected to the reservation page


  @validationMessages
  Scenario Outline: Verify validation messages for different input errors
    Given the user is on the table booking page
    When the user applies filters for location "<location>", guest count "<guest_count>", timeslot "<timeslot>", and date "<date>"
    Then a validation message "<expected_message>" is displayed

    Examples:
      | location                                           | guest_count | timeslot   | date        | expected_message                  |
      |                                                    | 4           | 10:30    | 20-05-2025  | Location is required              |
      | 123 Main Street, Downtown, New York, NY 10001      | 20          | 10:30    | 20-05-2025  | No table found                    |
      | 123 Main Street, Downtown, New York, NY 10001      | 4           | 23:00  | 20-05-2025   | No table found           |
      | 123 Main Street, Downtown, New York, NY 10001      | 4           | 12:00   |            | Date is required               |