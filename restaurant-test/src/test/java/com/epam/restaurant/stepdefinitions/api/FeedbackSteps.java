package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.pojos.FeedbackPojo;
import com.epam.restaurant.util.api.TestDataUtil;
import com.epam.restaurant.utils.api.ApiHelper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertFalse;

public class FeedbackSteps {

    private FeedbackPojo feedbackRequest;
    private Response response;
    private String authToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://restaurantapi-run8team16-sb-dev.development.krci-dev.cloudmentor.academy";
    }

    @Given("the user is authenticated")
    public void theUserIsAuthenticated() {
        String email = "auto@gmail.com";
        String password = "Password123!";
        authToken = ApiHelper.loginAndGetAuthToken(email, password);
        assertNotNull("Authentication failed. Token should not be null.", authToken);
    }



    @When("the user submits feedback with missing ratings for reservationId {string}")
    public void the_user_submits_feedback_with_missing_ratings(String reservationId) {
        feedbackRequest = new FeedbackPojo("6f47e2e6", "Updated cuisine comment", "4", "Updated service comment", "");
        feedbackRequest.setReservationId(reservationId);
        feedbackRequest.setCuisineComment("Missing ratings");
        feedbackRequest.setServiceComment("Missing ratings");

        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(feedbackRequest)
                .when()
                .post("/feedbacks");
    }

    @When("the user submits feedback without reservation ID")
    public void the_user_submits_feedback_without_reservation_id() {
        feedbackRequest = new FeedbackPojo("", "Updated cuisine comment", "4", "Updated service comment", "5");
        feedbackRequest.setCuisineComment("No reservation ID");
        feedbackRequest.setCuisineRating("4");
        feedbackRequest.setServiceComment("No reservation ID");
        feedbackRequest.setServiceRating("5");

        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(feedbackRequest)
                .when()
                .post("/feedbacks");
    }

    @When("the user updates feedback for reservationId {string}")
    public void the_user_updates_feedback(String reservationId) {
        feedbackRequest = new FeedbackPojo(
                reservationId,
                "Updated cuisine comment",
                "4",
                "Updated service comment",
                "5"
        );

        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(feedbackRequest)
                .when()
                .put("/feedbacks");
    }

    @When("the user retrieves feedback for location {string}")
    public void the_user_retrieves_feedback_for_location(String locationId) {
        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .when()
                .get("locations/"+locationId+"/feedbacks?type=cuisine");
    }

    @When("the user attempts to view feedback for reservation {string}")
    public void the_user_attempts_to_view_feedback_for_reservation(String reservationId) {
        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .when()
                .get("/feedbacks/" + reservationId);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the message should be {string}")
    public void the_message_should_be(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        assert actualMessage.equals(expectedMessage) : "Expected: " + expectedMessage + ", but got: " + actualMessage;
    }

    @Then("the feedback list should not be empty")
    public void the_feedback_list_should_not_be_empty() {
        List<FeedbackPojo> feedbacks = response.jsonPath().getList("id");
        assertNotNull(feedbacks.toString(), "Feedback list is null");
        assertFalse(feedbacks.isEmpty(), "Feedback list is empty");
    }

    @Given("reservation with ID {string} is in status {string}")
    public void reservation_with_id_is_in_status(String reservationId, String status) {
        TestDataUtil.setReservationStatus(reservationId, status);
        System.out.println("Mocked reservation ID: " + reservationId + " with status: " + status);
    }

    @When("the user submits feedback with cuisine rating {string} and service rating {string}")
    public void theUserSubmitsFeedbackWithCuisineRatingAndServiceRating(String cuisineRating, String serviceRating) {
        feedbackRequest = new FeedbackPojo(
                "6f47e2e6",
                "Delicious cuisine",
                cuisineRating,
                "Great service",
                serviceRating
        );

        response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(feedbackRequest)
                .when()
                .post("/feedbacks");
    }
}
