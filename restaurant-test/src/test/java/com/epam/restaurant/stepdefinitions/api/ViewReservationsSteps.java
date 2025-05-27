package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewReservationsSteps {

    private static final Logger logger = LoggerFactory.getLogger(ViewReservationsSteps.class);
    private Response response;
    private String authToken;

    @Given("I log in with valid username {string} and password {string}")
    public void iLogInWithValidUsernameAndPassword(String email, String password) {
        authToken = ApiHelper.loginAndGetAuthToken(email, password);

        Assert.assertNotNull("Auth token must not be null after login!", authToken);
        logger.info("Successfully logged in. Auth token retrieved: {}", authToken);
    }

    @When("I send a request to fetch reservations with the token")
    public void iSendARequestToFetchReservationsWithTheToken() {
        response = ApiHelper.getReservations(authToken);

        Assert.assertNotNull("Failed to fetch reservations! Response is null.", response);
        logger.info("Fetch reservations response received. Status code: {}", response.getStatusCode());
        logger.info("Fetch reservations response body: \n{}", response.asPrettyString());
    }

    @Then("I should receive reservation response with {int} status code and valid details")
    public void iShouldReceiveAStatusCodeAndValidateReservationIds(int expectedStatusCode) {
        Assert.assertNotNull("Response object is null. Cannot verify status code.", response);

        logger.info("Expected status code: {}, Actual status code: {}", expectedStatusCode, response.getStatusCode());

        if (response.getStatusCode() == 401) {
            logger.error("Unauthorized response received from the backend.");
            logger.error("Response body: {}", response.asPrettyString());
            Assert.fail("Expected status code " + expectedStatusCode + " but got 401 (Unauthorized).");
        }

        Assert.assertEquals("Unexpected status code for reservation response!", expectedStatusCode, response.getStatusCode());
    }

    @And("the response should match the reservations schema")
    public void theResponseShouldMatchTheReservationsSchema() {

        String schemaPath = "schemas/view-reservations-list-schema.json";

        Assert.assertNotNull("Response object is null. Cannot perform schema validation!", response);

        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        logger.info("Reservation response successfully validated against schema: {}", schemaPath);
    }

    @Given("the system is setup to handle requests with null tokens")
    public void theSystemIsSetupToHandleRequestsWithNullTokens() {
        logger.info("System is prepared to handle requests with null tokens.");
    }

    @When("I send a request to fetch reservations with a null token")
    public void iSendARequestToFetchReservationsWithANullToken() {
        response = ApiHelper.getReservationsWithNullToken();

        Assert.assertNotNull("Response object is null after sending request with null token!", response);
        logger.info("Fetch reservations with null token response received:\n{}", response.asPrettyString());
    }

    @Then("I should receive reservation response with  {int} status code")
    public void iShouldReceiveAStatusCode(int expectedStatusCode) {

        Assert.assertNotNull("Response object is null. Cannot verify status code.", response);
        Assert.assertEquals("Unexpected status code when fetching reservations with null token!", expectedStatusCode, response.getStatusCode());
        logger.info("Received expected status code is: {}", expectedStatusCode);
    }

    @And("the reservation response should contain an appropriate error message {string}")
    public void theResponseShouldContainAnAppropriateErrorMessage(String expectedErrorMessage) {

        String actualErrorMessage = response.jsonPath().getString("message");

        Assert.assertNotNull("Error message is missing in the response!", actualErrorMessage);
        Assert.assertEquals("Unexpected error message in the response!",
                expectedErrorMessage, actualErrorMessage);

        logger.info("Validated error message: Expected: '{}', Actual: '{}'", expectedErrorMessage, actualErrorMessage);
    }

}