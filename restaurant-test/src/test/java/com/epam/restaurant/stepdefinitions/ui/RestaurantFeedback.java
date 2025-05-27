package com.epam.restaurant.stepdefinitions.ui;

import com.epam.restaurant.config.ConfigManager;
import com.epam.restaurant.ui.pages.HomePage;
import com.epam.restaurant.ui.pages.LoginPage;
import com.epam.restaurant.ui.pages.ProfilePage;
import com.epam.restaurant.ui.pages.RegistrationPage;
import com.epam.restaurant.utils.ui.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class RestaurantFeedback {

    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage;
    private HomePage homePage;
    private ProfilePage profilePage;
    private final ConfigManager ConfigReader = new ConfigManager();
    private RegistrationPage registrationPage;
    private static final Logger logger = Logger.getLogger(LoginSteps.class.getName());

    @Given("The user is signed in successfully")
    public void theUserIsSignedInSuccessfully() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        logger.info("Navigating to the login page...");
        driver.get(ConfigReader.getProperty("login.url"));
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

    @Given("The user launches the application in the browser")
    public void theUserLaunchesTheApplicationInTheBrowser() {

        logger.info("Navigating to main page.");

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isUserOnHomePage(),
                "Login failed, user is not redirected to the home page!");
        logger.info("Application launched successfully with the correct title.");
    }

    @When("The user observes the main page")
    public void theUserObservesTheMainPage() {
        logger.info("Observing the main page...");
        String expectedtitle = "Green & Tasty";
        String actualTitle= homePage.getTitle();
        Assert.assertEquals( actualTitle,expectedtitle,"Main page title mismatch.");
    }

    @Then("The main page loads successfully and general application information is displayed")
    public void theMainPageLoadsSuccessfully() {
        logger.info("Verifying main content...");
        WebElement mainContent = driver.findElement(By.xpath("//a[contains(text(),'Reservation')]"));
        Assert.assertTrue(mainContent.isDisplayed(), "Main content not displayed on the page.");
        logger.info("Main page loaded successfully.");
    }

    @Given("The user is on the main page")
    public void theUserIsOnTheMainPage() {
        String expectedtitle = "Green & Tasty";
        String actualTitle= homePage.getTitle();
        Assert.assertEquals( actualTitle,expectedtitle,"Main page title mismatch.");
        logger.info("User is verified to be on the main page.");
    }

    @When("The user views the list of available restaurant locations")
    public void theUserViewsTheListOfAvailableRestaurantLocations() {
        logger.info("Locating restaurant locations...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> locations = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bg-white shadow-md hover:cursor-pointer transition delay-150 duration-300 ease-in-out hover:scale-90 rounded-lg p-4']")));
        System.out.println(locations.size());
        Assert.assertTrue(locations.size() > 0, "No restaurant locations found.");
        logger.info("Restaurant locations successfully located.");
    }

    @Then("All restaurant locations are displayed accurately")
    public void allRestaurantLocationsAreDisplayedAccurately() {
        logger.info("Verifying visibility of all restaurant locations...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> locations = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bg-white shadow-md hover:cursor-pointer transition delay-150 duration-300 ease-in-out hover:scale-90 rounded-lg p-4']")));
        for (WebElement location : locations) {
            Assert.assertTrue(location.isDisplayed(), "Restaurant location not displayed.");
        }
        logger.info("All restaurant locations are displayed accurately.");
    }

    @Given("The restaurant locations are displayed on the main page")
    public void theRestaurantLocationsAreDisplayedOnTheMainPage() {
        logger.info("Verifying if restaurant locations are displayed...");
        theUserViewsTheListOfAvailableRestaurantLocations();
    }

    @When("The user clicks on a restaurant location")
    public void theUserClicksOnARestaurantLocation() {
        logger.info("Clicking on a restaurant location...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> locations = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bg-white shadow-md hover:cursor-pointer transition delay-150 duration-300 ease-in-out hover:scale-90 rounded-lg p-4']")));
        locations.get(0).click();
        logger.info("Restaurant location clicked.");
    }

    @Then("The user is redirected to the Location Overview page")
    public void theUserIsRedirectedToTheLocationOverviewPage() {
        logger.info("Verifying redirection to the Location Overview page...");
        Assert.assertTrue(driver.getCurrentUrl().contains("location/loc3"), "Redirection failed!");
        logger.info("Successfully redirected to the Location Overview page.");
    }

    @Given("The user is on the Location Overview page")
    public void theUserIsOnTheLocationOverviewPage() {
        logger.info("Navigating to the Location Overview page...");
        theUserClicksOnARestaurantLocation();
    }

    @When("The user scrolls to the feedback section")
    public void theUserScrollsToTheFeedbackSection() {
        logger.info("Scrolling to feedback section...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement feedbackSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Customer Reviews']")));
        Assert.assertTrue(feedbackSection.isDisplayed(), "Feedback section not displayed.");
        logger.info("Feedback section successfully scrolled to and displayed.");
    }

    @Then("Feedback is displayed")
    public void feedbackIsDisplayed() {
        logger.info("Verifying feedback display...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        List<WebElement> feedbacks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bg-white shadow-md rounded-lg p-4']")));
        Assert.assertTrue(!feedbacks.isEmpty(), "No feedbacks displayed.");
        logger.info("Feedbacks are successfully displayed.");
    }

    @When("The user selects a filter option for feedback")
    public void theUserSelectsAFilterOptionForFeedback() {
        logger.info("Selecting filter option for feedback...");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(15));
        WebElement filterOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='bg-green-100 text-green-700 px-4 py-2 rounded-md flex items-center space-x-2']")));
        filterOption.click();
        logger.info("Filter option selected.");
    }

    @Then("Feedback is filtered accordingly, showing only relevant reviews")
    public void feedbackIsFilteredAccordinglyShowingOnlyRelevantReviews() {
        logger.info("Verifying filtered feedback...");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(15));
        List<WebElement> feedbacks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bg-white shadow-md rounded-lg p-4']")));
        Assert.assertTrue(!feedbacks.isEmpty(), "Filtered feedback is not displayed.");
        logger.info("Feedback filtered successfully.");
    }

    @When("The user sorts feedback by date or rating")
    public void theUserSortsFeedbackByDateOrRating() {
        logger.info("Sorting feedback...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'bg-green-100') and contains(text(),'')]")));
        sortDropdown.click();

        By newestButtonLocator = By.xpath("//button[normalize-space(text())='Newest first']");

        wait.until(ExpectedConditions.visibilityOfElementLocated(newestButtonLocator));
        WebElement sortDate = wait.until(ExpectedConditions.elementToBeClickable(newestButtonLocator));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortDate);
        logger.info("Clicking the 'Newest first' button to sort feedback...");
        sortDate.click();

        logger.info("Feedback successfully sorted.");
    }

    @Then("Feedback is sorted correctly based on the selected criterion")
    public void feedbackIsSortedCorrectlyBasedOnTheSelectedCriterion() {
        logger.info("Verifying feedback sorting...");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(15));
        List<WebElement> sortedFeedback = driver.findElements(By.xpath("//div[@class='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6']"));
        Assert.assertTrue(!sortedFeedback.isEmpty(), "Feedback sorting failed!");
        logger.info("Feedback sorted correctly.");
    }

    @When("The user uses the pagination controls")
    public void theUserUsesThePaginationControls() {
        logger.info("Using pagination controls...");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(12));
        WebElement nextPageButton = driver.findElement(By.xpath("//button[text()='Next']"));
        wait.until(ExpectedConditions.elementToBeClickable(nextPageButton)).click();

        logger.info("Successfully navigated to the next page.");
    }

    @Then("Feedback for the selected page is displayed correctly")
    public void feedbackForTheSelectedPageIsDisplayedCorrectly() {
        logger.info("Verifying feedback display for the selected page...");
        WebElement pageContent = driver.findElement(By.xpath("//button[text()='Service']"));
        Assert.assertTrue(pageContent.isDisplayed(), "Feedback for selected page is not displayed!");
        logger.info("Feedback for the selected page is displayed correctly.");
    }

    @And("Feedback is populated on the Location Overview page")
    public void feedbackIsPopulatedOnTheLocationOverviewPage() {
        logger.info("Navigating to the Location Overview page...");
        theUserClicksOnARestaurantLocation();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement feedbackSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Customer Reviews']")));
        if(feedbackSection.isDisplayed()) {

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'bg-green-100') and contains(@class, 'text-green-700')]")));
            dropdown.click();

            WebElement newestFirstButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Newest first']")));
            newestFirstButton.click();


            logger.info("Feedback is successfully populated and displayed.");
        }
    }
    @After
    public void tearDown() {
        logger.info("Closing browser...");
        if (driver != null) {
            driver.quit();
        }
    }
    @And("The feedback section contains multiple review pages")
    public void the_feedback_section_contains_multiple_review_pages() {
        logger.info("Navigating to the Location Overview page...");
        theUserClicksOnARestaurantLocation();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement feedbackSection = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Customer Reviews')]"))
        );
        Assert.assertTrue(feedbackSection.isDisplayed(), "Feedback section is not visible!");
        logger.info("Feedback section is visible on the Location Overview page.");

        logger.info("Checking if pagination controls are available...");
        List<WebElement> paginationControls = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//div[@class='flex justify-center mt-6 space-x-2']/button"))
        );
        Assert.assertTrue(!paginationControls.isEmpty(), "Pagination controls are not displayed in the feedback section!");
        logger.info("Pagination is available with " + paginationControls.size() + " controls.");

        WebElement nextButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Next')]"))
        );
        Assert.assertTrue(nextButton.isDisplayed(), "Next button for pagination is not available!");

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
        logger.info("Pagination is confirmed for multiple review pages.");
    }



    @And("Filters have been applied in the feedback section")
    public void filtersHaveBeenAppliedInTheFeedbackSection() {
        logger.info("Validating feedback filters are applied...");
        theUserClicksOnARestaurantLocation();


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement filterOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Service']")));
        filterOption.click();

        WebElement appliedFilter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='pb-2 font-medium border-b-2 border-green-500 text-green-600' and contains(text(),'Service')]")));
        Assert.assertTrue(appliedFilter.isDisplayed(), "Filter 'Service Experience' is not successfully applied.");

        logger.info("Filter 'Service Experience' has been successfully applied.");
    }

    @When("The user applies sorting to the filtered feedback")
    public void theUserAppliesSortingToTheFilteredFeedback() {
        logger.info("User attempts to sort the filtered feedback...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='bg-green-100 text-green-700 px-4 py-2 rounded-md flex items-center space-x-2']")));
        sortDropdown.click();
        logger.info("Sorting dropdown opened.");

        WebElement sortByNewest = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Newest first']")));
        sortByNewest.click();

        WebElement activeSortButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Newest first']")));
        Assert.assertTrue(activeSortButton.isDisplayed(), "Sorting 'Newest First' is not applied properly.");

        logger.info("Feedback successfully sorted by 'Newest First'.");
    }

    @Then("Feedback is displayed based on both filters and sorting options")
    public void feedbackIsDisplayedBasedOnBothFiltersAndSortingOptions() {
        logger.info("Validating that feedback is displayed as per applied filters and sorting...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement feedbackSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(@class,'border-green-500') and contains(text(),'Service')]")
        ));
        Assert.assertTrue(feedbackSection.isDisplayed(), "Feedback section is not displayed!");
        logger.info("Feedback section is visible.");

        List<WebElement> feedbackItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='bg-white shadow-md rounded-lg p-4']")
        ));

        Assert.assertFalse(feedbackItems.isEmpty(), "No filtered feedback is displayed!");
        logger.info("Number of feedback items found: " + feedbackItems.size());

        boolean expectedContentFound = false;
        for (WebElement item : feedbackItems) {
            String feedbackText = item.getText();
            logger.info("Feedback item: " + feedbackText);
            wait.until(ExpectedConditions.visibilityOf(item));
            if (feedbackText.toLowerCase().contains("was") || feedbackText.toLowerCase().contains("service")) {
                expectedContentFound = true;
                break;
            }
        }

        Assert.assertTrue(expectedContentFound,
                "Filtered feedback does not contain expected keywords like 'waiter' or 'service'.");

        logger.info("Filtered and sorted feedback is displayed correctly.");
    }

    @When("The user checks the restaurant's overall rating")
    public void theUserChecksTheRestaurantSOverallRating() {
        logger.info("User checks the restaurant's overall rating...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement ratingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='ml-auto flex items-center text-yellow-500 font-bold']")));
        Assert.assertTrue(ratingElement.isDisplayed(), "Restaurant overall rating is not visible!");

        logger.info("Restaurant's overall rating is visible.");
    }
    @Then("The selected restaurant's rating is displayed")
    public void theSelectedRestaurantSRatingIsDisplayed() {
        logger.info("Validating the restaurant's overall rating...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement ratingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='ml-auto flex items-center text-yellow-500 font-bold']")));

        String restaurantRating = ratingElement.getText();
        Assert.assertFalse(restaurantRating.isEmpty(), "Restaurant rating is not displayed!");

        try {
            double ratingValue = Double.parseDouble(restaurantRating);
            Assert.assertTrue(ratingValue >= 1.0 && ratingValue <= 5.0, "Restaurant rating is out of range (1-5): " + ratingValue);
        } catch (NumberFormatException e) {
            Assert.fail("Restaurant rating is not a valid number: " + restaurantRating);
        }

        logger.info("Restaurant's overall rating is displayed as: " + restaurantRating);
    }
}