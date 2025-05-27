package com.epam.restaurant.stepdefinitions.api;

import com.epam.restaurant.utils.api.ApiHelper;
import com.epam.restaurant.pojos.SigninPojo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class SignInSteps {

    private Response response;
    private SigninPojo user;

    @Given("I have user credentials with email {string} and password {string}")
    public void i_have_user_credentials(String email, String password) {
        this.user = new SigninPojo.Builder()
                .setEmail(email)
                .setPassword(password)
                .build();
    }

    @When("I send a login request")
    public void i_send_a_login_request() {
        response = ApiHelper.SigninUser(user);
    }

    @Then("I should get a login response with status code {int}")
    public void iShouldGetAResponseWithStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Unexpected status code!");
    }

    @Then("the role in the response should be {string}")
    public void theRoleInTheResponseShouldBe(String expectedRole) {
        response.then().assertThat()
                .statusCode(200)
                .body("role", equalToIgnoringCase(expectedRole));
    }

    @Then("the login response should match user login schema")
    public void schemaValidation() {
        String schemaPath = "schemas/user-login-schema.json";
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
        System.out.println("Response successfully validated against schema: " + schemaPath);
    }

    @Then("the login response should match the expected message {string}")
    public void responseMessageValidation(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertNotNull(actualMessage, "The message field is missing in the response!");
        Assert.assertEquals(actualMessage, expectedMessage, "Unexpected response message!");
    }
}
