package com.epam.restaurant.stepdefinitions.ui;

import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.HomePage;
import com.epam.restaurant.utils.ui.DriverFactory;
import com.epam.restaurant.utils.ui.LoggerUtils;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;

public class CommonSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private HomePage homePage;
    private final Logger logger = LoggerUtils.getLogger(CommonSteps.class);
    private final ConfigManager config = new ConfigManager();


    @Then("the user should be redirected to the user dashboard")
    public void the_User_Should_Be_Redirected_To_The_User_Dashboard() {

        HomePage homePage = new HomePage(driver);
        logger.info("Validating user redirection to the dashboard.");
        Assert.assertTrue(homePage.isUserOnHomePage(), "User dashboard was not displayed!");
    }
    @Then("should see a welcome message {string}")
    public void the_User_Should_See_A_Welcome_Message(String expectedMessage) {
        HomePage homePage = new HomePage(driver);
        String actualMessage = homePage.getWelcomeMessage();
        logger.info("Verifying welcome message. Expected: '{}', Actual: '{}'", expectedMessage, actualMessage);
        Assert.assertEquals(actualMessage, expectedMessage, "Welcome message doesn't match!");
    }
}
