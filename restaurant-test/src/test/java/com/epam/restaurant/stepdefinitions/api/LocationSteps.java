package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.List;
import java.util.Map;

public class LocationSteps {

    private Response response;

    @Given("the restaurant locations service is available")
    public void theRestaurantLocationsServiceIsAvailable() {
        System.out.println("Restaurant locations service is available.");
    }

    @When("the user sends a GET request to retrieve locations")
    public void theUserSendsAGETRequestToRetrieveLocations() {
        response = ApiHelper.getLocations();
        Assert.assertNotNull("The GET request to retrieve locations returned a null response!", response);
    }

    @Then("the locations response status code should be {int}")
    public void theLocationsResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assert.assertEquals("Unexpected status code! Response Body: " + response.body().asString(),
                expectedStatusCode, response.getStatusCode());
    }

    @And("the response should contain a list of locations")
    public void theResponseShouldContainAListOfLocations() {

        List<Map<String, Object>> locations = response.jsonPath().getList("$");


        Assert.assertNotNull("Response JSON is null or empty!", locations);
        Assert.assertFalse("The list of locations is empty!", locations.isEmpty());

        System.out.println("Locations received: " + locations);
    }

    @And("the response should match the expected locations schema")
    public void theResponseShouldMatchTheExpectedLocationsSchema() {
        String schemaPath = "schemas/listOf-locations-schema.json";

        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        System.out.println("Response matches the expected schema: " + schemaPath);
    }
}