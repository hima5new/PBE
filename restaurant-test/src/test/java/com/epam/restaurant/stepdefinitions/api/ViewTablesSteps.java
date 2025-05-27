package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class ViewTablesSteps {

    private static final Logger logger = LoggerFactory.getLogger(ViewTablesSteps.class);
    private Response response;

    @Given("I am ready to fetch available tables")
    public void iAmReadyToFetchAvailableTables() {
        logger.info("The test is ready to fetch available tables.");
    }

    @When("I send a request to fetch available tables with locationId {string}, date {string}, time {string}, and guests {int}")
    public void iSendARequestToFetchAvailableTablesWithAllInputs(String locationId, String date, String time, int guests) {
        response = ApiHelper.getAvailableTables(locationId, date, time, guests);
        Assert.assertNotNull("Response object is null!", response);
        logger.info("Response received for locationId: {}, date: {}, time: {}, guests: {}.", locationId, date, time, guests);
    }

    @When("I send a request to fetch available tables with locationId {string}, date {string}, and guests {int} without time")
    public void iSendARequestToFetchAvailableTablesWithoutTime(String locationId, String date, int guests) {
        response = ApiHelper.getAvailableTablesWithoutTime(locationId, date, guests);
        Assert.assertNotNull("Response object is null!", response);
        logger.info("Response received for locationId: {}, date: {}, guests: {}, no time.", locationId, date, guests);
    }

    @When("I send a request to fetch available tables with missing parameters")
    public void iSendARequestToFetchAvailableTablesWithMissingParameters(DataTable dataTable) {
        Map<String, Object> params = dataTable.asMap(String.class, Object.class);
        response = ApiHelper.getAvailableTablesWithMissingParams(params);
        Assert.assertNotNull("Response object is null!", response);
        logger.info("Response received with missing parameters: {}", params);
    }

    @When("I send a request to fetch available tables without query parameters")
    public void iSendARequestToFetchAvailableTablesWithoutQueryParameters() {
        response = ApiHelper.getAvailableTablesWithMissingParams(null);
        Assert.assertNotNull("Response object is null!", response);
        logger.info("Response received without any query parameters.");
    }

    @Then("I should receive a {int} status code and the response has valid schema")
    public void iShouldReceiveAStatusCodeAndTheResponseShouldContainAvailableTables(int expectedStatusCode) {

        Assert.assertEquals("Unexpected status code! Response Body: " + response.body().asString(), expectedStatusCode, response.getStatusCode());

        Object availableTables = response.jsonPath().get("availableTables");
        Assert.assertNotNull("Available tables must not be null! Response Body: " + response.body().asString(), availableTables);

        logger.info("Available tables received: {}", response.jsonPath().getList("availableTables"));

        String schemaPath = "schemas/view-available-tables-schema.json";
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        logger.info("Response successfully validated against schema: {}", schemaPath);
    }

    @Then("I should receive a {int} status code and an error message {string}")
    public void iShouldReceiveAStatusCodeAndAnErrorMessage(int expectedStatusCode, String expectedErrorMessage) {

        Assert.assertEquals("Unexpected status code! Response Body: " + response.body().asString(), expectedStatusCode, response.getStatusCode());

        String actualErrorMessage = response.jsonPath().getString("message");
        Assert.assertNotNull("Error message is missing in the response!", actualErrorMessage);
        Assert.assertFalse("Error message should not be empty!", actualErrorMessage.isEmpty());
        Assert.assertEquals("Error message does not match the expected value!", expectedErrorMessage, actualErrorMessage);

        logger.info("Validated error message: Expected: '{}', Actual: '{}'", expectedErrorMessage, actualErrorMessage);
    }
}