package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class SpecialDishesSteps {

    private static final Logger logger = LoggerFactory.getLogger(SpecialDishesSteps.class);
    private Response response;
    private String locationId;

    @Given("the user provides the restaurant location id {string}")
    public void theUserProvidesTheRestaurantLocationId(String id) {
        this.locationId = id;
        logger.info("User-provided restaurant location ID: {}", locationId);
    }

    @When("I send a GET request to retrieve special dishes for the location")
    public void iSendAGetRequestToRetrieveSpecialDishesForTheLocation() {
        response = ApiHelper.getSpecialityDishes(locationId);

        if (response == null) {
            Assert.fail("The GET request returned a null response. Something went wrong.");
        }
        logger.info("Response received: {}", response.asPrettyString());
    }

    @Then("the special dishes response status code should be {int}")
    public void verifySpecialDishesStatusCode(int expectedStatusCode) {
        Assert.assertNotNull(response, "Response object is null.");
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Unexpected status code for special dishes response.");
        logger.info("Verified special dishes response status code: {}", expectedStatusCode);
    }

    @Then("the special dishes response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String expectedErrorMessage) {

        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid location.");

        logger.info("Raw Response Body: {}", response.asPrettyString());

        String actualErrorMessage;
        try {
            actualErrorMessage = response.jsonPath().getString("message");
        } catch (Exception e) {
            logger.error("Failed to extract 'message' field from the response JSON.", e);
            Assert.fail("Error message field is missing or malformed: " + response.asPrettyString());
            return;
        }

        Assert.assertNotNull(actualErrorMessage, "Error message is missing in the response.");
        Assert.assertFalse(actualErrorMessage.isEmpty(), "Error message should not be empty.");
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage,
                "Error message does not match the expected value.");

        logger.info("Validated error message: Expected: '{}', Actual: '{}'", expectedErrorMessage, actualErrorMessage);
    }

    @And("the response should match the special dishes schema")
    public void theResponseShouldMatchTheSpecialDishesSchema() {
        String schemaPath = "schemas/special-dishes-schema.json";
        Assert.assertNotNull(response, "Response object is null. Cannot perform schema validation.");

        try {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            logger.info("Response successfully validated against schema: {}", schemaPath);
        } catch (Exception e) {
            logger.error("Schema validation failed!", e);
            throw new AssertionError("Schema validation failed: " + e.getMessage(), e);
        }
    }
}