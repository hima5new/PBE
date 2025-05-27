package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import com.epam.restaurant.pojos.AuthenticationPojo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class SignUpSteps {

    private AuthenticationPojo user;
    private Response response;

    @Given("a user with firstName {string}, lastName {string}, email {string}, and password {string}")
    public void user_details(String firstName, String lastName, String email, String password) {
        this.user = new AuthenticationPojo.Builder()
                .setFirstName(firstName.isEmpty() ? null : firstName)
                .setLastName(lastName.isEmpty() ? null : lastName)
                .setEmail(email.isEmpty() ? null : email)
                .setPassword(password.isEmpty() ? null : password)
                .build();
    }

    @When("I submit the registration request")
    public void send_registration_request() {
        response = ApiHelper.registerUser(user);
    }

    @Then("I should receive registration response with status code {int} and message {string}")
    public void validate_response(int expectedStatusCode, String expectedMessage) {
        assertEquals("Unexpected status code received!", expectedStatusCode, response.getStatusCode());

        String actualMessage = response.jsonPath().getString("message");
        assertNotNull("Message field is missing in the response!", actualMessage);
        assertEquals("Unexpected response message!", expectedMessage, actualMessage);
    }
}