@api
Feature: Customer Table Reservation
  Validate table reservation functionality for customers

  Background:
    Given the customer logs in with email "srija_shan@gmail.com" and password "Password123!"

  @smoke
  Scenario Outline: Validate table reservation with various inputs
    When the customer books a table with the following details:
      | locationId   | tableNumber   | date       | guestsNumber | timeFrom | timeTo  |
      | <locationId> | <tableNumber> | <date>     | <guestsNumber> | <timeFrom> | <timeTo> |
    Then the server should respond with status code <responseCode>
    And status code is 201, the response should include valid id
    And the response should match the table reservation schema

    Examples:
      | Test Case                   | locationId | tableNumber | date       | guestsNumber | timeFrom | timeTo | responseCode |
      | Successful reservation      | loc1       | T4          | 2025-06-15 | 8            | 10:30    | 12:00  | 201          |
      | Reservation within capacity | loc1       | T5          | 2025-06-15 | 1            | 10:30    | 12:00  | 201          |

  @regression
  Scenario Outline: Validate table reservation with various inputs
    When the customer books a table with the following details:
      | locationId   | tableNumber   | date       | guestsNumber | timeFrom | timeTo  |
      | <locationId> | <tableNumber> | <date>     | <guestsNumber> | <timeFrom> | <timeTo> |
    Then the server should respond with status code <responseCode>
    And the exact response message should be "<responseMessage>"

    Examples:
      | Test Case                     | locationId | tableNumber | date       | guestsNumber | timeFrom | timeTo | responseCode | responseMessage                                                                                                                              |
      | Exceeding table capacity      | loc1       | T5          | 2025-06-15 | 14           | 12:15    | 13:45  | 400          | Number of guests (14) exceeds table capacity of 10                                                                                           |
      | Time slot unavailable         | loc1       | T2          | 2025-06-15 | 4            | 10:30    | 12:00  | 400          | The requested time slot is not available for this table                                                                                      |
      | Invalid location              | loc12      | T3          | 2025-06-15 | 6            | 10:30    | 12:00  | 404          | Location with ID loc12 not found                                                                                                             |
      | Missing locationId            |            | T3          | 2025-06-15 | 6            | 10:30    | 12:00  | 400          | Location is required.                                                                                                                        |
      | Missing tableNumber           | loc1       |             | 2025-06-15 | 6            | 10:30    | 12:00  | 500          | Cannot invoke \"String.isBlank()\" because the return value of \"com.epam.edai.run8.team16.dto.ReservationRequest.getTableNumber()\" is null |
      | Missing date                  | loc1       | T3          |            | 6            | 10:30    | 12:00  | 500          | Cannot invoke \"String.isBlank()\" because the return value of \"com.epam.edai.run8.team16.dto.ReservationRequest.getDate()\" is null        |
      | Missing guestsNumber          | loc1       | T3          | 2025-06-15 |              | 10:30    | 12:00  | 400          | Number of guests is required                                                                                                                 |
      | Missing timeFrom              | loc1       | T3          | 2025-06-15 | 6            |          | 12:00  | 400          | Start time is required                                                                                                                       |
      | Missing timeTo                | loc1       | T3          | 2025-06-15 | 6            | 10:30    |        | 400          | End time is required                                                                                                                         |
      | Invalid tableNumber           | loc1       | T24         | 2025-06-15 | 6            | 10:30    | 12:00  | 404          | Table not found                                                                                                                              |
      | Past date reservation         | loc1       | T3          | 2025-05-13 | 6            | 10:30    | 12:00  | 400          | Reservations can only be made for today or future dates.                                                                                     |
      | Today's date reservation      | loc1       | T3          | 2025-05-27 | 6            | 10:30    | 12:00  | 400          | For same-day reservations, booking must be at least 30 minutes in advance.                                                                   |
      | Invalid time slot             | loc1       | T3          | 2025-06-15 | 6            | 10:15    | 11:45  | 400          | Invalid time slot. Start time: 10:15, End time: 11:45                                                                                        |
      | More than 30 days reservation | loc1       | T1          | 2025-07-19 | 2            | 10:30    | 12:00  | 400          | Reservations can only be made up to 30 days in advance.                                                                                      |

  @regression
  Scenario: Validate table reservation for unauthenticated user
    Given the customer is not authenticated
    When the customer books a table with the following details:
      | locationId | tableNumber | date      | guestsNumber | timeFrom | timeTo |
      | loc1       | T4          | 2025-06-15 | 8            | 10:30    | 12:00  |
    Then the server should respond with status code 401
    And the exact response message should be "Please sign in to continue"