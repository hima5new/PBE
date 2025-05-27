package com.epam.restaurant.stepdefinitions.ui;


import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.HomePage;
import com.epam.restaurant.ui.pages.LoginPage;
import com.epam.restaurant.utils.ui.DriverFactory;
import com.epam.restaurant.utils.ui.LoggerUtils;
import com.epam.restaurant.utils.ui.SessionManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;
import java.util.List;
import java.util.Map;

public class LoginSteps {

    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage;
    private HomePage homePage;
    private final Logger logger = LoggerUtils.getLogger(LoginSteps.class);
    private final ConfigManager config = new ConfigManager();

    @Given("the user is on the application")
    public void the_user_is_on_the_application() {
        String appUrl = config.getProperty("application.url");
        driver.get(appUrl);
        logger.info("Navigated to the application URL: {}", appUrl);
        loginPage = new LoginPage(driver);
    }

    @Given("The user is on the login page")
    public void the_user_is_on_the_login_page() {
        String loginurl = config.getProperty("login.url");
        driver.get(loginurl);
        logger.info("Navigated to the login page.");
        loginPage = new LoginPage(driver);
    }

    @When("the user navigates to the login page")
    public void the_user_navigates_to_the_login_page() {
        String loginUrl = config.getProperty("login.url");
        DriverFactory.getDriver().get(loginUrl);
        loginPage = new LoginPage(DriverFactory.getDriver());
        logger.info("Navigated to the login page.");
    }
    @When("The user clicks on the {string} button")
    public void the_user_clicks_on_the_button(String Sign_in) {

        logger.info("Submitting the login form.");
        loginPage.clickLoginButton();

    }
    @Then("A success message {string} should display")
    public void a_success_message_should_display(String successMessage) {
        HomePage homePage = new HomePage(driver);
        String actualMessage = homePage.getWelcomeMessage();
        logger.info("Verifying welcome message. Expected: '{}', Actual: '{}'", successMessage, actualMessage);
        Assert.assertEquals(actualMessage, successMessage, "Welcome message doesn't match!");
    }

    @Then("the login page is displayed successfully with email and password fields and the {string} button")
    public void the_login_page_is_displayed_successfully(String buttonName) {
        Assert.assertTrue(loginPage.isEmailFieldDisplayed(), "Email field is not displayed.");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field is not displayed.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), buttonName + " button is not displayed.");
    }

    @Then("An error message {string} should display")
    public void an_error_message_should_display(String expectedMessagePart) {

        if(expectedMessagePart.contains("Email address is required. Please enter your email to continue"))
        {
            String emailMessage = loginPage.getEmailErrorMessage();
            logger.info("Error is related to email. Validating partial message...");
            Assert.assertTrue(emailMessage.contains(expectedMessagePart), "Email error message mismatch. Expected part of message not found.");
            logger.info("Email error message validated successfully.");
        }
        if (expectedMessagePart.contains("Password is required for sign-in")) {
            String passwordMessage = loginPage.getErrorMessage();
            logger.info("Error is related to password. Validating partial message...");
            Assert.assertTrue(passwordMessage.contains(expectedMessagePart), "Password error message mismatch. Expected part of message not found.");
            logger.info("Password error message validated successfully.");
        }
        else {
            String actualMessage = loginPage.getErrorMessage();
            logger.info("Validating error message. Expected (Partial): '{}', Actual: '{}'", expectedMessagePart, actualMessage);
            logger.info("Error message is of a general nature. Validating partial message...");
            Assert.assertEquals(actualMessage, expectedMessagePart,"General error message mismatch. Expected part of message not found.");
            logger.info("General error message validated successfully.");
        }
    }

    @When("the user enters an invalid email as {string} and any password")
    public void the_user_enters_invalid_email(String email) {
        loginPage.enterEmail(email);
        loginPage.enterPassword("placeholder-password");
        logger.info("Entered invalid email '{}' and placeholder password.", email);
        loginPage.clickLoginButton();
    }

    @When("The user leaves the email and password fields empty")
    public void the_user_leaves_the_email_and_password_fields_empty() {
        loginPage.emailField.clear();
        loginPage.passwordField.clear();
        logger.info("Cleared the email and password fields.");
        loginPage.clickLoginButton();
    }
    @Then("The login page should display the email field")
    public void the_login_page_should_display_the_email_field() {

        Assert.assertTrue(loginPage.isEmailFieldDisplayed(), "The email field is not displayed on the login page.");
        logger.info("Verified the presence of the email field.");
    }
    @Then("The login page should display the password field")
    public void the_login_page_should_display_the_password_field() {

        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "The password field is not displayed on the login page.");
        logger.info("Verified the presence of the password field.");
    }
    @Then("The login page should display the {string} button")
    public void the_login_page_should_display_the_button(String buttonName) {

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),"The " + buttonName + " button is not displayed on the login page.");
        logger.info("Verified the presence of the '" + buttonName + "' button.");
    }
    @Then("the following UI elements should be present:")
    public void the_following_ui_elements_should_be_present(List<String> elements) {
        logger.info("Validating the presence of UI elements: {}", elements);
        for (String element : elements) {
            switch (element) {
                case "Email field":
                    Assert.assertTrue(loginPage.isEmailFieldDisplayed(), "Email field is not displayed.");
                    break;
                case "Password field":
                    Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field is not displayed.");
                    break;
                case "Login button":
                    Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed.");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown UI element: " + element);
            }
        }
    }
    @Then("the error message {string} should be displayed below the email field")
    public void the_error_message_should_be_displayed(String expectedMessage) {
        String actualMessage = loginPage.getEmailErrorMessage();
        logger.info("Validating error message for email field. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
        Assert.assertEquals(actualMessage, expectedMessage, "Email error message mismatch.");
    }

    @Then("Error messages should display as:")
    public void error_messages_should_be_displayed(Map<String, String> errors) {
        logger.info("Validating error messages: {}", errors);
        for (Map.Entry<String, String> error : errors.entrySet()) {
            String field = error.getKey();
            String expectedMessage = error.getValue();
            String actualMessage;
            switch (field) {
                case "Email":
                    actualMessage = loginPage.getEmailErrorMessage();
                    Assert.assertEquals(actualMessage, expectedMessage, "Mismatch for email error message");
                    break;
                case "Password":
                    actualMessage = loginPage.getPasswordErrorMessage();
                    Assert.assertEquals(actualMessage, expectedMessage, "Mismatch for password error message");
                    break;
                default:
                    throw new IllegalArgumentException("Field mismatch for error validations: " + field);
            }
        }
    }
    @Then("The user should remain logged in")
    public void the_user_should_remain_logged_in() {

         HomePage homePage = new HomePage(driver);
        logger.info("Validating user session persistence across browser closes.");
        Assert.assertTrue(homePage.isVisitorOnHomePage(), "User session did not persist.");
    }
    @When("The user logs in with email {string} and password {string}")
    public void the_user_logs_in_with_email_and_password(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        logger.info("Filling the login form with inputs: {}", data);
        for (Map<String, String> row : data){
            loginPage.enterEmail(row.get("Email"));
            loginPage.enterPassword(row.get("Password"));
            loginPage.clickLoginButton();
        }
    }
    @When("The user closes and reopens the browser")
    public void the_user_closes_and_reopens_the_browser() {

        logger.info("Saving session cookies before closing the browser...");
        SessionManager.saveSessionCookies(driver);

        logger.info("Closing the browser...");
        DriverFactory.quitDriver();


        logger.info("Re-initializing browser...");
        driver = DriverFactory.initDriver("chrome");

        logger.info("Navigating to the application URL...");
        String appUrl = config.getProperty("application.url");
        driver.get(appUrl);

        logger.info("Restoring cookies to preserve session...");
        SessionManager.restoreSessionCookies(driver);

        driver.navigate().refresh();

        logger.info("Browser reopened successfully and session verified.");
    }

    @When("The user enters email {string} and password {string}")
    public void the_user_enters_email_and_password(String email, String password) {
        logger.info("Entering user credentials - Email: {}, Password: [HIDDEN]", email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @When("The user enters emails {string} and password {string}")
    public void theUserEntersEmailsAndPassword(String email, String password) {
         logger.info("Entering user credentials - Email: {}, Password: [HIDDEN]", email);
            loginPage.enterEmail(email);
            loginPage.enterPassword(password);
            loginPage.clickLoginButton();

    }

    @Given("The user is logged in")
    public void theUserIsLoggedIn() {
        logger.info("Logging into the application...");
        DriverFactory.initDriver("chrome");
        WebDriver driver = DriverFactory.getDriver();

        driver.get(config.getProperty("login.url"));
        loginPage = new LoginPage(driver);

        loginPage.enterEmail(config.getProperty("valid.email"));
        loginPage.enterPassword(config.getProperty("valid.password"));
        loginPage.clickLoginButton();

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isUserOnHomePage(), "Login failed, user not redirected to home page!");
        logger.info("User is logged in successfully.");
    }
    @When("The user clicks the {string} button")
    public void theUserClicksTheButton(String button) {
        if ("Logout".equalsIgnoreCase(button)) {
            logger.info("Clicking on the Logout button...");
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButton();
        } else {
            throw new IllegalArgumentException("Button not recognized: " + button);
        }
    }
    @Then("The user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        logger.info("Verifying redirection to the login page...");
        Assert.assertTrue(loginPage.isEmailFieldDisplayed(), "The user is not on the login page after logout!");
        logger.info("User successfully redirected to login page.");

    }
}