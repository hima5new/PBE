package com.epam.restaurant.stepdefinitions.ui;

import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.LoginPage;
import com.epam.restaurant.ui.pages.RegistrationPage;
import com.epam.restaurant.utils.ui.DriverFactory;
import com.epam.restaurant.utils.ui.LoggerUtils;
import io.cucumber.datatable.DataTable;
import com.epam.restaurant.ui.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class RegistrationSteps {

    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private HomePage homePage;

    private final Logger logger = LoggerUtils.getLogger(RegistrationSteps.class);
    private final ConfigManager config = new ConfigManager();

    @Given("the user navigate to the registration page")
    public void the_User_Navigate_to_the_Registration_Page() {
        String registrationUrl = config.getProperty("registration.url");
        DriverFactory.getDriver().get(registrationUrl);
        logger.info("Navigated to the registration page: {}", registrationUrl);
        registrationPage = new RegistrationPage(DriverFactory.getDriver());
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(registrationUrl));
    }

    @When("the user fill in the registration form with valid inputs")
    public void the_User_Fill_The_Registration_Form_Valid_Inputs(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        logger.info("Filling the registration form with valid inputs: {}", data);
        for (Map<String, String> row : data) {
            registrationPage.enterFirstName(row.get("First Name"));
            registrationPage.enterLastName(row.get("Last Name"));
            registrationPage.enterEmail(row.get("Email"));
            registrationPage.enterPassword(row.get("Password"));
            registrationPage.enterConfirmPassword(row.get("Confirm Password"));
        }
    }

    @When("the user fill in the registration form with the following inputs:")
    public void the_User_Fill_The_Registration_Form_With_Inputs(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        logger.info("Filling the registration form with inputs: {}", data);
        for (Map<String, String> row : data) {
            registrationPage.enterFirstName(row.get("First Name"));
            registrationPage.enterLastName(row.get("Last Name"));
            registrationPage.enterEmail(row.get("Email"));
            registrationPage.enterPassword(row.get("Password"));
            registrationPage.enterConfirmPassword(row.get("Confirm Password"));
        }
    }

    @And("submit the registration form")
    public void submit_The_Registration_Form() {
        logger.info("Submitting the registration form.");
        registrationPage.clickRegister();
    }
    @Then("the user should see a success message {string}")
    public void the_User_Should_See_a_Success_Message(String expectedMessage) {
        String actualMessage = registrationPage.getSuccessMessage();
        logger.info("Verifying success message. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
       Assert.assertEquals(actualMessage, expectedMessage, "Success message doesn't match!");
    }

    @Then("the user should see an error message {string}")
    public void the_User_Should_See_An_Error_Message(String expectedMessage) {
        String actualMessage;

        if (expectedMessage.contains("Invalid email address")) {
            actualMessage = registrationPage.getEmailErrorMessage();
            logger.error("Validating email error. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
            Assert.assertEquals(actualMessage, expectedMessage, "Email error doesn't match!");
        }
        else if (expectedMessage.contains("First name can be")) {
            actualMessage = registrationPage.getNameErrorMessage();
            logger.error("Validating name error. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
            Assert.assertEquals(actualMessage, expectedMessage, "Name error doesn't match!");
        }
        else if (expectedMessage.contains("one special character required")) {
            actualMessage = registrationPage.getPasswordError();
            logger.error("Validating name error. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
            Assert.assertEquals(actualMessage, expectedMessage, "Name error doesn't match!");
        }
        else if(expectedMessage.contains("one special character required"))
        {
            actualMessage = registrationPage.getConfirmPasswordErrorMessage();
            logger.error("Validating name error. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
            Assert.assertEquals(actualMessage, expectedMessage, "Confirm error doesn't match!");
        }
        else {
            actualMessage = registrationPage.getErrorMessage();
            logger.info("Validating toast error. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
            Assert.assertEquals(actualMessage, expectedMessage, "Toast error doesn't match!");
        }
    }
    @Given("the user navigate to the login page")
    public void the_User_Navigate_To_The_Login_Page() {
        String loginUrl = config.getProperty("login.url");
        DriverFactory.getDriver().get(loginUrl);
        logger.info("Navigated to the login page: {}", loginUrl);
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @When("the user log in with registered credentials")
    public void the_User_Log_in_With_Credentials(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        logger.info("Logging in with credentials...");
        for(Map<String, String> row : data) {
            loginPage.enterEmail(row.get("Email"));
            loginPage.enterPassword(row.get("Password"));
        }
        loginPage.clickLoginButton();
    }
    @Then("the user should be redirected to the login page")
    public void the_user_should_be_redirected_to_the_login_page() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        logger.info("Validating user redirection to the login page.");
        Assert.assertTrue(loginPage.emailField.isDisplayed(), "User dashboard was not displayed!");

    }
}