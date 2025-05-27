package com.epam.restaurant.stepdefinitions.ui;

import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.HomePage;
import com.epam.restaurant.ui.pages.LoginPage;
import com.epam.restaurant.ui.pages.ProfilePage;
import com.epam.restaurant.ui.pages.RegistrationPage;
import com.epam.restaurant.util.ui.EmailListMock;
import com.epam.restaurant.utils.ui.DriverFactory;
import com.epam.restaurant.utils.ui.LoggerUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;

public class RoleAccessSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;
    private final Logger logger = LoggerUtils.getLogger(RoleAccessSteps.class);
    private final ConfigManager ConfigReader = new ConfigManager();
    private RegistrationPage registrationPage;

    @Given("The email {string} exists in the Waiter's email list")
    public void the_email_exists_in_the_waiters_email_list(String email) {
        logger.info("Checking if email '{}' exists in the Waiter's email list...", email);
        if (!EmailListMock.isEmailInWaiterList(email)) {
            throw new AssertionError("Email '" + email + "' is not present in the Waiter's email list.");
        }
        logger.info("Email '{}' exists in the Waiter's email list.", email);
    }

    @Given("The email {string} does NOT exist in the Waiter's email list")
    public void the_email_does_not_exist_in_the_waiters_email_list(String email) {
        logger.info("Checking if email '{}' does NOT exist in the Waiter's email list...", email);
        if (EmailListMock.isEmailInWaiterList(email)) {
            throw new AssertionError("Email '" + email + "' is incorrectly present in the Waiter's email list.");
        }
        logger.info("Email '{}' does NOT exist in the Waiter's email list.", email);
    }

    @Given("The user is on the registration page")
    public void the_user_is_on_the_registration_page() {
        logger.info("Navigating to the registration page...");
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("registration.url"));
        registrationPage = new RegistrationPage(driver);
        registrationPage.isUserOnRegistrationPage();
        logger.info("Registration page loaded successfully.");
    }

    @When("The user registers with the email {string} and provides valid inputs")
    public void the_user_registers_with_the_email_and_provides_valid_inputs(String email) {
        logger.info("Registering user with email '{}'...", email);
        registrationPage.enterEmail(email);
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterPassword("ValidPassword@123");
        registrationPage.enterConfirmPassword("ValidPassword@123");
        registrationPage.clickRegister();
        logger.info("Registration form submitted successfully.");
    }

    @When("The user registers with the invalid email {string}")
    public void the_user_registers_with_the_invalid_email(String email) {
        logger.info("Attempting registration with invalid email '{}'...", email);
        registrationPage.enterEmail(email);
        registrationPage.enterFirstName("Invalid");
        registrationPage.enterLastName("User");
        registrationPage.enterPassword("ValidPassword@123");
        registrationPage.enterConfirmPassword("ValidPassword@123");
        registrationPage.clickRegister();
        logger.info("Invalid registration form submitted.");
    }

    @Then("The system shows a validation error message {string}")
    public void the_system_shows_a_validation_error_message(String expectedMessage) {
        logger.info("Checking for validation error message '{}'.", expectedMessage);
        String actualMessage = registrationPage.getEmailErrorMessage();
        Assert.assertEquals(actualMessage,expectedMessage,"Validation error message mismatch!");
        logger.info("Validation error message verified successfully: {}", actualMessage);
    }
    @When("The user logs in with the waiter email and password")
    public void the_user_logs_in_with_the_waiter_email_and_password() {
        logger.info("Logging in the user...");
        driver= DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("login.url"));
        loginPage = new LoginPage(driver); // Initialize LoginPage
        loginPage.enterEmail("peter.parker@restaurant.com");
        loginPage.enterPassword("Password123!");
        loginPage.clickLoginButton();
        logger.info("User logged in successfully.");
    }

    @When("The user logs in with the customer email and password")
    public void the_user_logs_in_with_the_customer_email_and_password() {
        logger.info("Logging in the user...");
        driver= DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("login.url"));
        loginPage = new LoginPage(driver);
        loginPage.enterEmail("vaishnavi_srividya2603@gmail.com");
        loginPage.enterPassword("Vaishnavi26@");
        loginPage.clickLoginButton();
        logger.info("User logged in successfully.");
        logger.info("Validating successful login...");
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isUserOnHomePage(),
                "Login failed, user is not redirected to the home page!");
        logger.info("User logged in successfully and is on the Home Page.");
    }

    @Then("The user is assigned the role {string}")
    public void the_user_is_assigned_the_role(String expectedRole) {
        if (this.profilePage == null) {
            profilePage = new ProfilePage(driver);
        }
        logger.info("Validating if the user is logged in...");

        Assert.assertTrue(profilePage.isProfilePageLoaded(),
                "User is not logged in. Profile Page is not loaded.");
        String actualRole = profilePage.getRoleFromProfile();
        Assert.assertEquals( actualRole,expectedRole,"Role assignment mismatch!");
        logger.info("User assigned role '{}' successfully.", actualRole);
    }

    @And("The role {string} is displayed in the user's profile")
    public void the_role_is_displayed_in_the_users_profile(String expectedRole) {
        logger.info("Validating role '{}' in profile page...", expectedRole);
        String roleDisplayed = profilePage.getRoleFromProfile();
        Assert.assertEquals(roleDisplayed,expectedRole,"Role displayed in the profile is incorrect!" );
        logger.info("Role '{}' is displayed correctly in the user's profile.", roleDisplayed);
    }

    @Given("The user has not registered or logged into the application")
    public void the_user_has_not_registered_or_logged_into_the_application() {
        logger.info("Opening the application as a visitor...");
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("application.url"));
        logger.info("User is accessing the application without registration or login.");
    }

    @When("The user accesses visitor-specific features")
    public void the_user_accesses_visitor_specific_features() {
        logger.info("Browsing visitor-specific features...");
        homePage = new HomePage(driver);
        boolean isVisitorFeatureAccessible = homePage.isVisitorOnHomePage();
        Assert.assertTrue(isVisitorFeatureAccessible, "Visitor features are not accessible!");
        logger.info("Visitor features verified successfully.");
    }

    @Then("The user is automatically assigned the role {string}")
    public void the_user_is_automatically_assigned_the_role(String expectedRole) {
        logger.info("Validating the automatically assigned role '{}' for the user.", expectedRole);
        String actualRole = "Visitor";
        Assert.assertEquals(actualRole, expectedRole,"Role mismatch for the visitor!");
        logger.info("The role '{}' was automatically assigned successfully.", actualRole);
    }

    @And("Visitor features are accessible without requiring registration or login")
    public void visitor_features_are_accessible_without_requiring_registration_or_login() {
        logger.info("Checking accessibility of visitor features...");
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("registration.url"));
        registrationPage = new RegistrationPage(driver);
        boolean isPublicFeatureAccessible = registrationPage.isUserOnRegistrationPage();
        Assert.assertTrue(isPublicFeatureAccessible, "Visitor features are not accessible!");
        logger.info("Visitor features are accessible without login or registration.");
    }

    @When("The user registers with the email {string} and logs in")
    public void the_user_registers_with_the_email_and_logs_in(String email) {
        logger.info("Starting registration and login process for email '{}'...", email);
        the_user_registers_with_the_email_and_provides_valid_inputs(email);
        the_user_logs_in_with_the_waiter_email_and_password();
    }

    @And("The user logs in with the email {string}")
    public void theUserLogsInWithTheEmail(String email) {
        logger.info("Starting login process for email '{}'...", email);
        the_user_logs_in_with_the_customer_email_and_password();
    }

    @Given("The user has an account with the email {string}")
    public void theUserHasAnAccountWithTheEmail(String email) {
        logger.info("Checking if the user has an account with the email '{}'...", email);
        boolean isWaiter = EmailListMock.isEmailInWaiterList(email);
        boolean isCustomer = EmailListMock.isEmailInCustomerList(email);

        if (!isWaiter && !isCustomer) {
            throw new AssertionError("No account exists for the email '" + email + "' in the system.");
        }
        if (isWaiter) {
            logger.info("Email '{}' is associated with the role 'Waiter'.", email);
        } else if (isCustomer) {
            logger.info("Email '{}' is associated with the role 'Customer'.", email);
        }
    }

    @Given("The email {string} exists in both Waiter and Customer lists")
    public void theEmailExistsInBothWaiterAndCustomerLists(String email) {
        logger.info("Checking if the user has an account with the email '{}'...", email);
        boolean isWaiter = EmailListMock.isEmailInWaiterList(email);
        boolean isCustomer = EmailListMock.isEmailInCustomerList(email);
        if (!isWaiter && !isCustomer) {
            throw new AssertionError("No account exists for the email '" + email + "' in the system.");
        }
        if(isWaiter && isCustomer)
        {
            logger.info("Email '{}' is associated with the role 'Waiter' and 'Customer'. ",email);
        }
    }

    @When("The user navigates to the profile page")
    public void theUserNavigatesToTheProfilePage() {
        HomePage homePage = new HomePage(DriverFactory.getDriver());

        ProfilePage profilePage = homePage.clickProfileLink();

        Assert.assertTrue(profilePage.isProfilePageLoaded(), "Profile Page did not load successfully.");
        logger.info("Navigated to Profile Page from Home Page.");
    }

    @Then("The user is assigned the role {string} because it has higher priority over {string}")
    public void theUserIsAssignedTheRoleBecauseItHasHigherPriorityOver(String customer, String waiter) {
        String email = "peter.parker@restaurant.com";
        boolean isInWaiterList = EmailListMock.isEmailInWaiterList(email);
        boolean isInCustomerList = EmailListMock.isEmailInCustomerList(email);

        Assert.assertTrue(isInWaiterList, "The email '" + email + "' must exist in the Waiter's email list.");
        Assert.assertTrue(isInCustomerList, "The email '" + email + "' must exist in the Customer's email list.");

        String assignedRole = EmailListMock.determineRole(email);
        Assert.assertEquals(  waiter,assignedRole,
                "The assigned role is not the higher priority role '" + waiter + "'.");

        logger.info("The email '{}' is correctly assigned to the higher priority role '{}'.", email, waiter);

    }

    @Then("The role {string} is displayed in the profile")
    public void theRoleIsDisplayedInTheProfile(String expectedRole) {

            logger.info("Validating that the role '{}' is displayed in the user's profile...", expectedRole);

            profilePage = homePage.navigateToProfile();

            String actualRole = profilePage.getRoleFromProfile();

            Assert.assertEquals(actualRole, expectedRole, "The role displayed in the profile is incorrect!");

            logger.info("The role '{}' is correctly displayed in the user's profile.", actualRole);

    }
}
