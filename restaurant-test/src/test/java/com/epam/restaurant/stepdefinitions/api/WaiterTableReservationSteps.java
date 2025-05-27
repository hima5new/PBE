package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import com.epam.restaurant.pojos.WaiterTableReservationPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import java.time.LocalDate;
import java.util.Map;

public class WaiterTableReservationSteps {

    private String authToken;
    private String endpoint;
    private Response response;
    private WaiterTableReservationPojo reservation;


    @Given("the endpoint {string}")
    public void the_endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @And("the valid waiter is logged in")
    public void the_valid_waiter_is_logged_in() {

        authToken = ApiHelper.loginAndGetAuthToken("peter.parker@restaurant.com", "Password123!");
    }

    @Given("the waiter provides valid inputs")
    public void the_waiter_provides_valid_inputs(Map<String, String> dataTable) {
        populateReservationFromTable(dataTable);
    }

    @Given("the waiter provides invalid inputs")
    public void the_waiter_provides_invalid_inputs(Map<String, String> dataTable) {
        populateReservationFromTable(dataTable);
    }

    @When("the user sends a POST request")
    public void the_user_sends_a_post_request() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("📤 Final JSON Payload: " + mapper.writeValueAsString(reservation));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        response = ApiHelper.waiterBookTable(reservation, authToken);

    }

    @Then("the status code should be {int}")
    public void the_status_code_should_be(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @And("the response should match the waiter reservation schema")
    public void the_response_should_match_the_waiter_reservation_schema() {
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/waiter-reservation-schema.json"));
    }

    @And("the user sends a DELETE request")
    public void the_user_sends_a_delete_request() {
        String reservationId = response.getBody().jsonPath().getString("reservationId");
        Response deleteResponse = ApiHelper.deleteReservation(reservationId, authToken);
        Assert.assertEquals(200, deleteResponse.statusCode());
    }

    @And("the response message should be {string}")
    public void the_response_message_should_be(String expectedMessage) {
        String actualMessage = response.getBody().jsonPath().getString("message");
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    private void populateReservationFromTable(Map<String, String> data) {
        System.out.println("🚨 Incoming data map: " + data);
        String date = "<tomorrow>".equalsIgnoreCase(data.get("date"))
                ? LocalDate.now().plusDays(1).toString()
                : data.get("date");

        String guestsNumber = data.getOrDefault("guestsNumber", "");
        String resolvedGuestsNumber = guestsNumber.isEmpty() ? "0" : String.valueOf(Integer.parseInt(guestsNumber));

        reservation = new WaiterTableReservationPojo.Builder()
                .setClientType(data.getOrDefault("clientType", ""))
                .setCustomerName(data.getOrDefault("customerName", ""))
                .setDate(date)
                .setGuestsNumber(resolvedGuestsNumber)
                .setLocationId(data.get("locationId"))
                .setTableNumber(data.get("tableNumber"))
                .setTimeFrom(data.get("timeFrom"))
                .setTimeTo(data.get("timeTo"))
                .build();
        System.out.println("✅ Built reservation object: " + reservation);
    }

    @And("is signed out")
    public void isSignedOut() {
        authToken = null;
    }
}
