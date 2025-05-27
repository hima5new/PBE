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
import java.util.List;

public class FeedbackWithLocationIDSteps {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackWithLocationIDSteps.class);
    private Response response;
    private String locationId;
    private String feedbackType;
    private String sortBy;

    @Given("I have location id {string} with feedback type {string} and sorting {string}")
    public void iHaveLocationIdWithFeedbackTypeAndSorting(String locationId, String feedbackType, String sortBy) {
        this.locationId = locationId;
        this.feedbackType = feedbackType;
        this.sortBy = sortBy;
        logger.info("Prepared test with locationId: {}, feedbackType: {}, sortBy: {}", locationId, feedbackType, sortBy);
    }

    @When("I send a request to retrieve feedback")
    public void iSendARequestToRetrieveFeedback() {
        response = ApiHelper.getLocationFeedback(locationId, feedbackType, sortBy);
        Assert.assertNotNull("Response object is null!", response);
        logger.info("Feedback retrieval response received. Response body:\n{}", response.asPrettyString());
    }

    @Then("I should receive a {int} status code and feedback details")
    public void iShouldReceiveAStatusCodeAndFeedbackDetails(int expectedStatusCode) {

        Assert.assertEquals("Invalid status code received", expectedStatusCode, response.getStatusCode());
        logger.info("Validated status code: {}", expectedStatusCode);

        logger.info("Feedback retrieval successful for locationId: {}", locationId);
    }

    @And("the response should match the feedback schema")
    public void theResponseShouldMatchTheFeedbackSchema() {

        String schemaPath = "schemas/feedback-with-locationId-schema.json";
        Assert.assertNotNull("Response object is null. Cannot validate schema.", response);

        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        logger.info("Response successfully validated against schema: {}", schemaPath);
    }

    @Then("I should receive a {int} status code for invalid feedback retrieval and an error message {string}")
    public void iShouldReceiveAStatusCodeForInvalidFeedbackRetrievalWithErrorMessage(int expectedStatusCode, String expectedErrorMessage) {

        Assert.assertEquals("Invalid status code received", expectedStatusCode, response.getStatusCode());
        logger.info("Validated status code for invalid feedback retrieval: {}", expectedStatusCode);

        String actualErrorMessage = response.jsonPath().getString("message");
        Assert.assertNotNull("Error message should be present", actualErrorMessage);
        Assert.assertEquals("Error message does not match the expected value!", expectedErrorMessage, actualErrorMessage);

        logger.info("Validated error message: {}", expectedErrorMessage);
    }

    @Then("I should receive a 400 status code for invalid feedback type retrieval and an error message {string}")
    public void iShouldReceiveAStatusCodeForInvalidFeedbackTypeRetrievalWithErrorMessage(String expectedErrorMessage) {

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals("Unexpected status code!", 400, actualStatusCode);
        System.out.println("Validated status code for invalid feedback type retrieval: " + actualStatusCode);


        String responseBody = response.body().asString();
        System.out.println("Response Body: " + responseBody);


        String actualErrorMessage = response.jsonPath().getString("message");
        Assert.assertNotNull("Error message should be present in response!", actualErrorMessage);
        Assert.assertTrue("Expected error message not found in response!",
                actualErrorMessage.contains(expectedErrorMessage));

        System.out.println("Validated error message: " + actualErrorMessage);
    }

    private boolean isSortedDescending(List<?> list) {
        return list.stream().sorted((o1, o2) -> ((Comparable) o2).compareTo(o1)).toList().equals(list);
    }

    private boolean isSortedAscending(List<?> list) {
        return list.stream().sorted((o1, o2) -> ((Comparable) o1).compareTo(o2)).toList().equals(list);
    }
}