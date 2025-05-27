package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DeleteReservationSteps {

    private static final Logger logger = LoggerFactory.getLogger(DeleteReservationSteps.class);

    private String authToken;
    private Response deleteResponse;

    @Given("I log in with email {string} and password {string}")
    public void logInWithEmailAndPassword(String email, String password) {

        authToken = ApiHelper.loginAndGetAuthToken(email, password);
        Assert.assertNotNull(authToken, "Auth token must not be null after login!");
        logger.info("Successfully logged in. Auth token retrieved: {}", authToken);
    }

    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        authToken = null;
        logger.info("User is not logged in. Auth token is null.");
    }

    @When("I send a DELETE request to cancel a reservation with id {string}")
    public void sendDeleteRequest(String reservationId) {

        deleteResponse = ApiHelper.deleteReservation(reservationId, authToken);
        Assert.assertNotNull(deleteResponse, "The DELETE response is null.");
        logger.info("DELETE request sent for reservation ID: {}. Received status code: {}", reservationId, deleteResponse.getStatusCode());
    }

    @When("I try to delete a reservation with id {string} as a non-logged-in user")
    public void sendDeleteRequestAsNonLoggedInUser(String reservationId) {

        deleteResponse = ApiHelper.deleteReservationWithNullAuthToken(reservationId);
        Assert.assertNotNull(deleteResponse, "The DELETE response is null.");
        logger.info("DELETE request sent for reservation ID: {} as a non-logged-in user. Received status code: {}", reservationId, deleteResponse.getStatusCode());
    }

    @Then("I should receive a response with status code {int} and message {string}")
    public void validateResponse(int expectedStatusCode, String expectedMessage) {

        Assert.assertEquals(deleteResponse.getStatusCode(), expectedStatusCode, "Unexpected HTTP status code.");
        logger.info("Verified status code: {}", expectedStatusCode);

        String actualMessage;

        String contentType = deleteResponse.getHeader("Content-Type");
        logger.info("Response Content-Type: {}", contentType);

        if (expectedStatusCode == 200 && contentType != null && contentType.contains("text/plain")) {

            actualMessage = deleteResponse.asString().trim();
        } else if (contentType != null && contentType.contains("application/json")) {

            actualMessage = deleteResponse.jsonPath().getString("message");
        } else {

            logger.warn("Unexpected Content-Type or missing Content-Type. Assuming plain text.");
            actualMessage = deleteResponse.asString().trim();
        }


        Assert.assertNotNull(actualMessage, "Response message is null or missing!");
        Assert.assertEquals(actualMessage, expectedMessage, "Mismatch in response message.");
        logger.info("Verified response message: {}", actualMessage);
    }
}