@ui
Feature: Restaurant Feedback and Navigation
  A user wants to access and interact with restaurant information, view feedback, and filter/sort reviews.

  Background:
    Given The user is signed in successfully
  @smoke
  Scenario: Main page accessibility is successful
    Given The user launches the application in the browser
    When The user observes the main page
    Then The main page loads successfully and general application information is displayed

  @smoke
  Scenario: Verify all the locations of the restaurant are displayed on the Main Page
    Given The user is on the main page
    When The user views the list of available restaurant locations
    Then All restaurant locations are displayed accurately

    @regression
  Scenario: Verify navigation to the Restaurant Location Overview page
    Given The restaurant locations are displayed on the main page
    When The user clicks on a restaurant location
    Then The user is redirected to the Location Overview page

  @regression
  Scenario: Verify feedback section display
    Given The user is on the Location Overview page
    When The user scrolls to the feedback section
    Then Feedback is displayed

  @smoke
  Scenario: Verify feedback filtering by service or cuisine experience
    Given The user is on the Location Overview page
    And Feedback is populated on the Location Overview page
    When The user selects a filter option for feedback
    Then Feedback is filtered accordingly, showing only relevant reviews

  @smoke
  Scenario: Verify feedback sorting by date or rating
    Given The user is on the Location Overview page
    And Feedback is populated on the Location Overview page
    When The user sorts feedback by date or rating
    Then Feedback is sorted correctly based on the selected criterion

  @regression,@smoke
  Scenario: Verify pagination in feedback section
    Given The user is on the Location Overview page
    And The feedback section contains multiple review pages
    When The user uses the pagination controls
    Then Feedback for the selected page is displayed correctly

  @regression
  Scenario: Verify feedback display with a combination of filters and sorting
    Given The user is on the Location Overview page
    And Filters have been applied in the feedback section
    When The user applies sorting to the filtered feedback
    Then Feedback is displayed based on both filters and sorting options

   @smoke
  Scenario: Verify restaurant rating display
    Given The user is on the Location Overview page
    When The user checks the restaurant's overall rating
    Then The selected restaurant's rating is displayed
