package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.List;
import java.util.Map;

public class LocationSelectOptionsSteps {

    private Response response;

    @Given("the location select options API is available")
    public void theLocationSelectOptionsApiIsAvailable() {
        Assert.assertNotNull("LOCATION_SELECT_ENDPOINT should be configured.", ApiHelper.getShortLocationDetails());
    }

    @When("I send a GET request to fetch short location details")
    public void iSendAGetRequestToFetchShortLocationDetails() {
        response = ApiHelper.getShortLocationDetails();
    }

    @Then("I should receive a {string} status code")
    public void iShouldReceiveAStatusCode(String expectedStatusCode) {
        Assert.assertEquals("Unexpected HTTP response status code!",
                Integer.parseInt(expectedStatusCode.split(" ")[0]), response.getStatusCode());
    }

    @Then("the response should contain a list of locations with addresses and ids")
    public void theResponseShouldContainAListOfLocationsWithAddressesAndIds() {

        List<Map<String, String>> locations = response.jsonPath().getList("$");


        Assert.assertNotNull("Response shouldn't be null", locations);
        Assert.assertFalse("Locations list should not be empty!", locations.isEmpty());


        locations.forEach(location -> {
            Assert.assertNotNull("Location ID should not be null!", location.get("id"));
            Assert.assertNotNull("Location address should not be null!", location.get("address"));
        });

        System.out.println("Locations received successfully: " + locations);
    }

    @Then("the location options response should match the location schema")
    public void theResponseShouldMatchTheLocationSchema() {
        String schemaPath = "schemas/locations-selectOptions-schema.json";
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));

        System.out.println("Response successfully validated against schema: " + schemaPath);
    }
}