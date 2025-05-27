@api
Feature: Table reservation system for waiters
  As a waiter
  I want to make table reservations for customers
  So that I can assist customers efficiently and handle possible errors

  Background:
    Given the endpoint "/bookings/waiter"
    And the valid waiter is logged in
  @requiresWaiterLogin
  Scenario Outline: Verify successful table booking by a waiter
    Given the waiter provides valid inputs
      | clientType    | <clientType>    |
      | customerName | <customerName>|
      | locationId    | <locationId>    |
      | tableNumber   | <tableNumber>   |
      | date          | <date>          |
      | timeFrom      | <timeFrom>      |
      | timeTo        | <timeTo>        |
      | guestsNumber  | <guestsNumber>  |
    When the user sends a POST request
    Then the status code should be <statusCode>
    And the response should match the waiter reservation schema
    And the user sends a DELETE request
    Examples:
      | testCase                     | clientType | customerName      | date       | guestsNumber | locationId | tableNumber | timeFrom | timeTo | statusCode |
      | Valid reservation - Customer | CUSTOMER   | yaswa,yaswa@gmail.com    | 2025-06-10 | 2            | loc1       | T3          | 17:30    | 19:00  | 200        |
      | Valid reservation - Visitor  | VISITOR    |  ,visitor            | <tomorrow> | 2            | loc1       | T1          | 10:30    | 12:00  | 200        |

  @valid
  Scenario Outline: Verify invalid reservation by a waiter
    Given the waiter provides invalid inputs
      | clientType    | <clientType>    |
      | customerName| <customerName>  |
      | locationId    | <locationId>    |
      | tableNumber   | <tableNumber>   |
      | date          | <date>          |
      | timeFrom      | <timeFrom>      |
      | timeTo        | <timeTo>        |
      | guestsNumber  | <guestsNumber>  |
    When the user sends a POST request
    Then the status code should be <statusCode>
    And the response message should be "<errorMessage>"

    Examples:
      | testCase                      | clientType | customerName | date       | guestsNumber | locationId | tableNumber | timeFrom | timeTo | statusCode | errorMessage                                               |
      | Missing customer name         | CUSTOMER   |                 | <tomorrow> | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | The requested table is not available for the specified time slot.                 |
      | TimeFrom after TimeTo         | VISITOR    | Visitor         | <tomorrow> | 2            | loc1       | T1          | 12:00    | 10:30  | 400        | The requested table is not available for the specified time slot.                        |
      | Unavailable slot selected     | VISITOR    | Visitor         | <tomorrow> | 2            | loc1       | T1          | 12:30    | 14:00  | 400        | The requested table is not available for the specified time slot.     |
      | Missing table number          | VISITOR    | Visitor         | <tomorrow> | 2            | loc1       |             | 10:30    | 12:00  | 400        | Table number cannot be empty                                |
      | Invalid table number          | VISITOR    | Visitor         | <tomorrow> | 2            | loc1       | T999        | 10:30    | 12:00  | 400        | Table T999 not found at location loc1                                |
      | Invalid locationId            | VISITOR    | Visitor         | <tomorrow> | 2            | locXYZ     | T1          | 10:30    | 12:00  | 400        | Waiter can only create reservations for their assigned location   |
      | Reject non-zero-padded dates  | VISITOR    | Visitor         | 2025-9-1   | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Invalid date format. Use ISO format (YYYY-MM-DD)            |
      | Invalid date format           | VISITOR    | Visitor         | 10-09-2025 | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Invalid date format. Use ISO format (YYYY-MM-DD)            |
      | Non date input                | VISITOR    | Visitor         | abcd       | 2            | loc1       | T1          | 10:30    | 12:00  | 400        |Invalid date format. Use ISO format (YYYY-MM-DD)            |
      | Missing date                  | VISITOR    | Visitor         |            | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Date cannot be empty                                        |
      | Invalid leap year date        | VISITOR    | Visitor         | 2026-02-29 | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Invalid date format. Use ISO format (YYYY-MM-DD)            |
      | Invalid calendar date (31-Sep)| VISITOR    | Visitor         | 2026-09-31 | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Invalid date format. Use ISO format (YYYY-MM-DD)            |
      | Booking with past date        | VISITOR    | Visitor         | 2024-01-01 | 2            | loc1       | T1          | 10:30    | 12:00  | 400        | Bookings can only be made between today and 30 days from today |
      | Missing guests number         | VISITOR    | Visitor         | <tomorrow> |              | loc1       | T1          | 10:30    | 12:00  | 400        | For input string: \"\"                                          |
      | Guests exceed capacity        | VISITOR    | Visitor         | <tomorrow> | 25           | loc1       | T1          | 10:30    | 12:00  | 400        | The requested table cannot accommodate 25 guests (capacity: 2)"                     |
      | Zero guest number             | VISITOR    | Visitor         | <tomorrow> | 0            | loc1       | T1          | 10:30    | 12:00  | 400        | Number of guests must be positive                     |
      | Negative guests number        | VISITOR    | Visitor         | <tomorrow> | -5           | loc1       | T1          | 10:30    | 12:00  | 400        | Number of guests must be positive                    |
      #| Slot already booked           | VISITOR    | Visitor         | 2025-12-31 | 2            | loc1       | T1          | 10:30    | 12:00  | 409        | Table already booked                                        |


  Scenario Outline: Verify unauthenticated waiter reservation attempt
    Given the waiter provides valid inputs
      | clientType    | <clientType>    |
      |  customerName | <customerName> |
      | date          | <date>          |
      | guestsNumber  | <guestsNumber>  |
      | locationId    | <locationId>    |
      | tableNumber   | <tableNumber>   |
      | timeFrom      | <timeFrom>      |
      | timeTo        | <timeTo>        |
    And is signed out
    When the user sends a POST request
    Then the status code should be <statusCode>
    And the response message should be "<message>"

    Examples:
      | testCase                     | clientType |customerName    | date       | guestsNumber | locationId |   tableNumber     | timeFrom      | timeTo           | statusCode | message                      |
      | Valid reservation - Customer | CUSTOMER   | yaswa yaswa@gmail.com   | <tomorrow> | 2            | loc1     |          T1        |   12:15       |  13:45      | 401        | Please sign in to continue  |

