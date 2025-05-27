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

public class PopularDishesSteps {

    private Response response;

    @Given("the popular dishes service is available")
    public void thePopularDishesServiceIsAvailable() {
        System.out.println("The popular dishes service is available.");
    }

    @When("the user sends a GET request to retrieve popular dishes")
    public void theUserSendsAGETRequestToRetrievePopularDishes() {
        response = ApiHelper.getPopularDishes();
        Assert.assertNotNull("The GET request to retrieve popular dishes returned a null response!", response);
    }

    @Then("the popular dish response status code should be {int}")
    public void thePopularDishResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assert.assertEquals("Unexpected status code! Response Body: " + response.body().asString(),
                expectedStatusCode, response.getStatusCode());
    }

    @And("the response should contain a list of popular dishes")
    public void theResponseShouldContainAListOfPopularDishes() {
        List<Map<String, Object>> popularDishes = response.jsonPath().getList("$");

        Assert.assertNotNull("Response should contain a valid list of dishes!", popularDishes);
        Assert.assertFalse("The list of popular dishes should not be empty!", popularDishes.isEmpty());

        System.out.println("Popular dishes received: " + popularDishes);
    }

    @And("every dish in the response should have the popular attribute set to true")
    public void everyDishInTheResponseShouldHaveThePopularAttributeSetToTrue() {
        List<Map<String, Object>> popularDishes = response.jsonPath().getList("$");

        Assert.assertNotNull("Response should contain a valid list of dishes!", popularDishes);
        Assert.assertFalse("The list of popular dishes should not be empty!", popularDishes.isEmpty());

        for (Map<String, Object> dish : popularDishes) {
            Assert.assertNotNull("Dish object should not be null!", dish);
            Boolean isPopular = (Boolean) dish.get("popular");
            Assert.assertNotNull("'popular' attribute should not be null for any dish!", isPopular);
            Assert.assertTrue("Every dish should have the 'popular' attribute set to true!", isPopular);
        }

        System.out.println("All dishes in the response have the 'popular' attribute set to true.");
    }

    @And("the response should match the popular dishes schema")
    public void theResponseShouldMatchThePopularDishesSchema() {

        String schemaPath = "schemas/popular-dishes-schema.json";
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));

        System.out.println("Response successfully validated against schema: " + schemaPath);
    }
}