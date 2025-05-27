package com.epam.restaurant.stepdefinitions.ui;


import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.BookTablePage;
import com.epam.restaurant.ui.pages.HomePage;
import com.epam.restaurant.ui.pages.LoginPage;
import com.epam.restaurant.utils.ui.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class ReservationAndTable {

    private  WebDriver driver = DriverFactory.getDriver();
    private final ConfigManager config = new ConfigManager();
    private  LoginPage loginPage ;
    private  HomePage homePage ;
    private  BookTablePage bookTablePage ;
    private static final Logger logger = Logger.getLogger(ReservationAndTable.class.getName());
    @Given("the user is logged in")
    public void the_user_is_logged_in() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        logger.info("Navigating to the login page...");
        driver.get(config.getProperty("login.url"));
        loginPage = new LoginPage(driver);
        logger.info("Entering login credentials...");
        loginPage.enterEmail("yaswa@gmail.com");
        loginPage.enterPassword("Anirwin@1234");

        logger.info("Clicking the login button...");
        loginPage.clickLoginButton();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isUserOnHomePage(),
                "Login failed, user is not redirected to the home page!");
        logger.info("User signed in successfully.");
    }

    @Given("the user navigates to Book a Table page")
    public void the_user_navigates_to_book_a_table_page() {

        bookTablePage = homePage.navigateToBookTable();
        assertTrue("Book Table page not loaded!", bookTablePage.isLocationDropdownDisplayed());
        logger.info("User is on the Book a Table page.");
    }
    @Given("tables exist in multiple locations")
    public void tables_exist_in_multiple_locations() {
        logger.info("Verifying that tables are available in multiple locations...");
        if (!bookTablePage.isLocationDropdownDisplayed()) {
            logger.severe("Location dropdown is not displayed.");
            throw new IllegalStateException("Location dropdown is not available on the page.");
        }
        logger.info("Locations are available");
    }

    @When("the user selects a location filter {string}")
    public void the_user_selects_a_location_filter(String location) {
        logger.info("Selecting location filter: " + location);
        bookTablePage.selectLocation(location);
    }

    @Then("only tables from the selected location {string} are displayed")
    public void only_tables_from_the_selected_location_are_displayed(String location) {
        logger.info("Verifying tables displayed are only for location: " + location);
        List<WebElement> tables = bookTablePage.getAllTableCards();
        for (WebElement table : tables) {
            assertTrue("Table does not match location!", table.getText().contains(location));
            logger.info("Validated table for location: " + table.getText());
        }
    }
    @Given("the user is on the {string} page")
    public void the_user_is_on_the_page(String pageTitle) {
        Assert.assertTrue(bookTablePage.isUserOnBookATable(), "User is not on the Book a Table page");
    }

    @Given("tables with varying guest capacities exist")
    public void tablesWithVaryingGuestCapacitiesExist() {
        logger.info("Verifying that table cards include guest capacity information...");


        List<WebElement> tableCards = bookTablePage.getAllTableCards();


        if (tableCards == null || tableCards.isEmpty()) {
            logger.severe("No table cards are displayed on the page.");
            throw new IllegalStateException("Table cards with guest capacities are required, but none are displayed.");
        }


        boolean hasGuestCapacity = false;
        for (WebElement tableCard : tableCards) {
            String cardText = tableCard.getText().toLowerCase();
            logger.info("Analyzing table card: " + cardText);


            if (cardText.contains("guest")) {
                hasGuestCapacity = true;
                break;
            }
        }


        if (!hasGuestCapacity) {
            logger.severe("No table card contains guest capacity information.");
            throw new IllegalStateException("None of the table cards contain guest capacity information.");
        }
        logger.info("Table cards include guest capacity information.");
    }
    @When("the user selects guest count {string}")
    public void the_user_selects_guest_count(String guestCountStr) {
        bookTablePage.selectLocation("123 Main Street, Downtown, New York, NY 10001");
        bookTablePage.setDate("24-05-2025");
        int guestCount = Integer.parseInt(guestCountStr);
        bookTablePage.setGuestCount(guestCount);
        bookTablePage.findATable.click();
    }


@Then("only tables that accommodate the specified guest count {string} are displayed")
public void only_tables_that_accommodate_the_specified_guest_count_are_displayed(String guestCountStr) {
    int expectedGuestCount = Integer.parseInt(guestCountStr);

    JavascriptExecutor js = (JavascriptExecutor) driver;


    List<String> capacityTexts = (List<String>) js.executeScript(
            "return Array.from(document.querySelectorAll('p'))." +
                    "filter(el => el.textContent.toLowerCase().includes('table seating capacity'))." +
                    "map(el => el.textContent);"
    );

    Assert.assertFalse(capacityTexts.isEmpty(), " No table capacity texts were found!");

    for (String text : capacityTexts) {
        System.out.println("Table capacity text: " + text.toLowerCase());

        Matcher matcher = Pattern.compile("(\\d+)").matcher(text);
        if (matcher.find()) {
            int actualCapacity = Integer.parseInt(matcher.group());
            System.out.println("Parsed capacity: " + actualCapacity);

            Assert.assertTrue(actualCapacity >= expectedGuestCount,
                    " Table card does not match guest count. Found: " + actualCapacity +
                            ", expected at least: " + expectedGuestCount);
        } else {
            Assert.fail("⚠️ Could not extract guest capacity from: \"" + text + "\"");
        }
    }
}


    @When("the user selects guest count {int}")
    public void theUserSelectsGuestCount(int guestCount) {
        logger.info("Setting guest count to: " + guestCount);
        bookTablePage.setGuestCount(guestCount);
    }

    @Then("only tables that accommodate the specified guest count {int} are displayed")
    public void onlyTablesThatAccommodateTheSpecifiedGuestCountAreDisplayed(int guestCount) {
        logger.info("Verifying that displayed tables accommodate guest count: " + guestCount);
        List<WebElement> tables = bookTablePage.getAllTableCards();
        for (WebElement table : tables) {
            assertTrue("Table does not match guest count!", table.getText().contains(guestCount + " guests"));
            logger.info("Validated table for guest count: " + guestCount + " - " + table.getText());
        }
    }

    @Given("tables are available in different timeslots")
    public void tablesAreAvailableInDifferentTimeslots() {
        logger.info("Verifying that tables with varying timeslots are available...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        List<WebElement> timeslotButton = driver.findElements(By.xpath(".//button[contains(.,'10:30')]"));////
        if(!timeslotButton.isEmpty())
        {
            logger.info("Time Slots are available");
        }
        logger.info("No Available Time Slots");
    }

    @When("the user selects a specific timeslot {string}")
    public void theUserSelectsASpecificTimeslot(String timeslot) {
        logger.info("Setting timeslot to: " + timeslot);
        bookTablePage.enterTimeslot(timeslot);
    }

    @Then("only available tables at the selected timeslot {string} are displayed")
    public void onlyAvailableTablesAtTheSelectedTimeslotAreDisplayed(String timeslot) {
        logger.info("Verifying tables displayed for timeslot: " + timeslot);
        List<WebElement> tables = bookTablePage.getAllTableCards();
        for (WebElement table : tables) {
            assertTrue("Table does not match timeslot!", table.getText().contains(timeslot));
            logger.info("Validated table for timeslot: " + timeslot + " - " + table.getText());
        }
    }

    @Given("the user is on the table booking page")
    public void theUserIsOnTheTableBookingPage() {
        logger.info("Navigating to the table booking page...");

        driver.get(config.getProperty("booktable.url"));

        assertTrue("Table booking page did not load correctly!", driver.getCurrentUrl().contains("bookTable"));
        logger.info("Successfully navigated to the table booking page.");
    }
    @When("the user applies filters for location {string}, guest count {string}, and timeslot {string}")
    public void theUserAppliesFiltersForLocationGuestCountAndTimeslot(String location, String guestCount, String timeslot) {
        logger.info("Applying filters with location: " + location + ", guest count: " + guestCount + ", and timeslot: " + timeslot);
        bookTablePage.applyFilters(location, Integer.parseInt(guestCount), timeslot);
    }
    @When("selects a table for {string} capacity and clicks an available timeslot")
    public void selectsATableAndClicksAnAvailableTimeslot(String guestCount) {
        logger.info("Selecting a table with capacity: " + guestCount + " and clicking an available timeslot...");

        boolean isTableSelected = bookTablePage.selectTableByGuestCount(Integer.parseInt(guestCount));

        assertTrue("No table with desired capacity and timeslot found!", isTableSelected);

        logger.info("Successfully selected a table and clicked on a timeslot.");
    }

    @Then("reservation cards with guest count {string} and a \"Make a Reservation\" button are displayed")
    public void reservationCardsWithReservationButtonAreDisplayed(String guestCount) {
        logger.info("Verifying that the reservation card shows guest count: " + guestCount + " and has 'Make a Reservation' button...");

        WebElement reservationCard = bookTablePage.getReservationCard();

        try {
            WebElement reservationButton = reservationCard.findElement(By.xpath(".//button[text()='Make a Reservation']"));
            assertTrue("Make a Reservation button is not displayed on the reservation card!", reservationButton.isDisplayed());
            logger.info("Reservation card has a 'Make a Reservation' button successfully displayed.");
        } catch (NoSuchElementException e) {
            logger.severe("Make a Reservation button is not found on the reservation card.");
            throw new AssertionError("Make a Reservation button is not found on the reservation card.");
        }
    }
    @When("the user applies filters to search tables")
    public void theUserAppliesFiltersToSearchTables() {
        logger.info("Applying filters. Example: location=New York, guestCount=2, timeslot=12:00 PM");
        bookTablePage.applyFilters("New York", 2, "12:00 PM");
    }

    @Then("booked tables are not displayed in the results")
    public void bookedTablesAreNotDisplayedInTheResults() {
        logger.info("Validating that booked tables are not displayed in the results...");
        List<WebElement> tables = bookTablePage.getAllTableCards();
        for (WebElement table : tables) {
            assertFalse("Booked table is displayed!", table.getText().contains("Booked"));
            logger.info("Validated table is not booked.");
        }
    }

    @Given("the user is viewing table availability")
    public void theUserIsViewingTableAvailability() {
        logger.info("User is now on the table availability page.");
    }

    @When("the user selects a table")
    public void theUserSelectsATable() {
        logger.info("Attempting to select a table with capacity=2 and timeslot=12:00 PM...");
        boolean isTableSelected = bookTablePage.selectTableAndClickTimeslot(2, "12:00 PM");
        assertTrue("No table with desired conditions found!", isTableSelected);
        logger.info("Table successfully selected.");
    }

    @When("clicks on the available timeslot")
    public void clicksOnTheAvailableTimeslot() {
        logger.info("Clicking on the available timeslot button...");
    }

    @Then("the user is redirected to the reservation page")
    public void theUserIsRedirectedToTheReservationPage() {
        logger.info("Making reservation and validating redirection to the reservation page...");
        bookTablePage.makeReservation();
        assertTrue(driver.getCurrentUrl().contains("bookings"));
        logger.info("Redirected to reservation page successfully.");
    }

    @When("the user enters invalid input in search filters {string}")
    public void theUserEntersInvalidInputInSearchFilters(String invalidInput) {
        logger.info("Applying filter with invalid input: " + invalidInput);
        bookTablePage.applyFilters(null, -1, null);
    }

    @Then("the system displays an error message saying {string}")
    public void theSystemDisplaysAnErrorMessageSaying(String errorMessage) {
        logger.info("Validating error message: " + errorMessage);
        String actualError = bookTablePage.getValidationMessage();
        assertEquals(errorMessage, actualError);
        logger.info("Error message validation successful: " + errorMessage);
    }
    @Then("a validation message {string} is displayed")
    public void aValidationMessageIsDisplayed(String expectedMessage) {
        logger.info("Verifying that the validation message is displayed...");

        String actualMessage = bookTablePage.getValidationMessage();

        assertEquals("Validation message does not match the expected value!", expectedMessage, actualMessage);
        logger.info("Validation message is correct: " + actualMessage);
    }
    @When("selects a table and clicks on an available timeslot")
    public void selectsATableAndClicksAnAvailableTimeslot() {
        logger.info("Selecting a table and clicking on an available timeslot...");

        boolean isTableSelected = bookTablePage.selectTableAndClickTimeslot(4, "12:15 - 13:45");
        assertTrue("Failed to select a table and timeslot!", isTableSelected);

        logger.info("Successfully selected a table and clicked on an available timeslot.");
    }

    @When("the user applies filters for location {string}, guest count {string}, timeslot {string}, and date {string}")
    public void theUserAppliesFiltersForLocationGuestCountTimeslotAndDate(String location, String guestCount, String timeslot, String date) {
        logger.info("Applying filters with location: " + location + ", guestCount: " + guestCount + ", timeslot: " + timeslot + ", and date: " + date);

        if (location != null && !location.isEmpty()) {
            bookTablePage.selectLocation(location);
        }

        if (guestCount != null && !guestCount.isEmpty()) {
            try {
                int guestCountValue = Integer.parseInt(guestCount);
                bookTablePage.setGuestCount(guestCountValue);
            } catch (NumberFormatException e) {
                logger.warning("Invalid guest count format: " + guestCount);
            }
        }

        if (timeslot != null && !timeslot.isEmpty()) {
            bookTablePage.enterTimeslot(timeslot);
        }

        if (date != null && !date.equalsIgnoreCase("invalidDate")) {
            bookTablePage.setDate(date);
        } else {
            logger.warning("Invalid date format was provided: " + date);
        }
        bookTablePage.findATable.click();
        logger.info("Filters applied successfully.");
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}