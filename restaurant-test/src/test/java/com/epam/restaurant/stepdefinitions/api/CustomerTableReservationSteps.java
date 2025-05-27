package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.pojos.CustomerTableReservationPojo;
import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.Map;

public class CustomerTableReservationSteps {

    private String authToken = null;
    private Response response;

    @Given("the customer logs in with email {string} and password {string}")
    public void authenticateCustomer(String email, String password) {
        authToken = ApiHelper.loginAndGetAuthToken(email, password);
        Assert.assertNotNull("Failed to authenticate the customer. Auth token must not be null.", authToken);
        System.out.println("Authenticated successfully. Token: " + authToken);
    }

    @Given("the customer is not authenticated")
    public void simulateUnauthenticatedCustomer() {
        authToken = null;
        System.out.println("Simulating unauthenticated customer...");
    }

    @When("the customer books a table with the following details:")
    public void bookTable(DataTable table) {
        Map<String, String> tableData = table.asMaps(String.class, String.class).get(0);
        CustomerTableReservationPojo reservation = new CustomerTableReservationPojo.Builder()
                .setLocationId(tableData.get("locationId"))
                .setTableNumber(tableData.get("tableNumber"))
                .setDate(tableData.get("date"))
                .setGuestsNumber(tableData.get("guestsNumber"))
                .setTimeFrom(tableData.get("timeFrom"))
                .setTimeTo(tableData.get("timeTo"))
                .build();
        response = ApiHelper.customerBookTable(reservation, authToken);
    }

    @Then("the server should respond with status code {int}")
    public void validateStatusCode(int expectedStatusCode) {
        Assert.assertEquals("Unexpected API response status code.", expectedStatusCode, response.getStatusCode());
    }

    @And("status code is 201, the response should include valid id")
    public void validateReservationIdOnSuccess() {
        if (response.getStatusCode() == 201) {
            String reservationId = response.jsonPath().getString("id");
            Assert.assertNotNull("Reservation ID must not be null for successful reservations.", reservationId);
            Assert.assertFalse("Reservation ID must not be empty for successful reservations.", reservationId.trim().isEmpty());
        } else {
            System.out.println("Skipping ID validation as status code is not 201.");
        }
    }

    @And("the exact response message should be {string}")
    public void validateResponseMessageForError(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertNotNull("Error message must not be null.", actualMessage);
        Assert.assertEquals("Unexpected error message from API.", expectedMessage, actualMessage);
    }

    @And("the response should match the table reservation schema")
    public void validateReservationSchemaOnSuccess() {
        String schemaPath = "schemas/table-reserved-byCustomer-schema.json";
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        System.out.println("Response successfully validated against schema: " + schemaPath);
    }
}