package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DishesApiSteps {

    private String authToken;
    private Response response;

    @Given("the user is signed in with valid credentials")
    public void theUserIsSignedInWithValidCredentials() {
        String email = "auto@gmail.com";
        String password = "Password123!";
        authToken = ApiHelper.loginAndGetAuthToken(email, password);
        assertNotNull("Authentication failed. Token should not be null.", authToken);
    }

    @Given("the user requests dishes of type {string}")
    public void theUserRequestsDishesOfType(String dishType) {
        response = ApiHelper.getDishesByType(dishType, authToken);
    }

    @Given("the user requests a dish with ID {int}")
    public void theUserRequestsDishById(int dishId) {
        response = ApiHelper.getDishById(dishId, authToken);
    }

    @Given("the user requests dishes sorted by {string} in {string} order for type {string}")
    public void theUserRequestsSortedDishes(String sortColumn, String sortOrder, String dishType) {
        response = ApiHelper.getDishesWithSorting(dishType, sortColumn, sortOrder, authToken);
    }

    @Given("the user requests dishes with invalid sort column {string} for type {string}")
    public void theUserRequestsDishesWithInvalidSort(String invalidSort, String dishType) {
        response = ApiHelper.getDishesInvalidSort(dishType, invalidSort, authToken);
    }

    @Given("the user requests dishes with a missing ID")
    public void theUserRequestsDishesWithMissingId() {
        response = ApiHelper.getDishesWithMissingId(authToken);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("the response contains {string}")
    public void theResponseContains(String expectedOutcome) {
        switch (expectedOutcome) {
            case "a list of dishes":
                response.then()
                        .body("content", notNullValue())
                        .body("content.size()", greaterThan(0));
                break;

            case "an empty list":
                response.then()
                        .body("content", notNullValue())
                        .body("content.size()", equalTo(0));
                break;

            case "an error message \"Invalid dish type(s). Allowed values: Appetizers, Main Courses, Desserts\"":
                String actualMessage = response.jsonPath().getString("message");
                assertEquals("Invalid dish type(s). Allowed values: Appetizers, Main Courses, Desserts", actualMessage);
                break;

            default:
                fail("Unexpected outcome: " + expectedOutcome);
        }
    }

    @Then("the response returns {string}")
    public void theResponseReturns(String expectedOutcome) {
        switch (expectedOutcome) {
            case "dish details with correct attributes":
                response.then()
                        .body("id", notNullValue())
                        .body("name", notNullValue())
                        .body("description", notNullValue())
                        .body("price", notNullValue())
                        .body("dishType", notNullValue());
                break;

            case "an error message 'Dish not found'":
                String actualErrorMessage = response.jsonPath().getString("message");
                assertEquals("Dish not found", actualErrorMessage);
                break;

            default:
                fail("Unexpected output: " + expectedOutcome);
        }
    }

    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        assertEquals("Error message mismatch", expectedMessage, actualMessage);
    }

    @Then("the response should contain an empty list")
    public void theResponseShouldContainAnEmptyList() {
        response.then()
                .body("content", notNullValue())
                .body("content.size()", equalTo(0));
    }

    @Then("the response contains dishes sorted by {string}")
    public void theResponseContainsDishesSortedBy(String sortingCriteria) {
        List<Integer> sortingField = response.jsonPath().getList("content." + sortingCriteria);
        assertTrue("Dishes are not sorted by " + sortingCriteria, isSortedAscending(sortingField));
    }

    private boolean isSortedAscending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}