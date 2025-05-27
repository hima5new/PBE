package com.epam.restaurant.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class BookTablePage {

    private WebDriver driver;

    public BookTablePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }



    @FindBy(xpath="//h1[text()='Book a Table']")
    private WebElement userOnBook;
    @FindBy(xpath = "//span[text()='Location']")
    private WebElement locationDropdown;

    @FindBy(xpath = "//button[@data-testid='decrement']")
    private WebElement incrementGuestCountButton;

    @FindBy(xpath = "//button[@data-testid='increment']")
    private WebElement decrementGuestCountButton;

    @FindBy(xpath = "//span[@data-testid='guestCount']")
    private WebElement guestCountField;


    @FindBy(xpath = "//input[@type='date']")
    private WebElement dateInput;

    @FindBy(xpath = "//input[@type='time']")
    private WebElement timeslotInput;

    @FindBy(xpath = "//div[@class='bg-white p-4 shadow-md rounded-lg flex flex-col md:flex-row h-full']")
    private List<WebElement> tableCards;


    @FindBy(xpath = "//div[@role='alert']")
    private WebElement validationMessage;

    @FindBy(xpath = "//button[@data-testid='closeButton']")
    private WebElement closeButton;

    @FindBy(xpath="//button[text()='Make a Reservation']")
    private WebElement reservationButton;

    @FindBy(xpath="//button[text()='Find a Table']")
    public WebElement findATable;
    public void setDate(String yyyyMMdd) {
        // Option 1
        dateInput.clear();
        dateInput.sendKeys(yyyyMMdd);

    }
    public boolean isUserOnBookATable()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(userOnBook));
        return userOnBook.isDisplayed();
    }

    public void selectLocation(String location) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locationDropdown);
            wait.until(ExpectedConditions.elementToBeClickable(locationDropdown)).click();

            String optionXPath = String.format("//li[contains(text(), '%s')]", location);

            WebElement locationOption = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(optionXPath))
            );

            locationOption.click();
        } catch (TimeoutException e) {
            throw new AssertionError("Failed to find or click on Location dropdown or option: " + location, e);
        }
    }



    public void setGuestCount(int desiredCount) {
        int currentCount = Integer.parseInt(guestCountField.getText().trim());

        while (currentCount < desiredCount) {

            incrementGuestCountButton.click();
            currentCount++;
        }

        while (currentCount > desiredCount) {

            decrementGuestCountButton.click();
            currentCount--;
        }
    }
    public boolean isLocationDropdownDisplayed() {
        try {
            return locationDropdown.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void enterTimeslot(String timeslot) {
        timeslotInput.clear();
        timeslotInput.sendKeys(timeslot);
    }


    public void applyFilters(String location, int guestCount, String timeslot) {
        if (location != null) selectLocation(location);
        if (guestCount > 0) setGuestCount(guestCount);
        if (timeslot != null) enterTimeslot(timeslot);
        findATable.click();

    }

    public List<WebElement> getAllTableCards() {
        return tableCards;
    }
    public void waitForReservationCards() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@data-testid, 'reservation-card')]")));
    }
    public void makeReservation()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(reservationButton));
        reservationButton.click();
    }
    public String getValidationMessage() {
        return validationMessage.getText();
    }
    public WebElement getReservationCard() {

            return driver.findElement(By.xpath("//h2[text()='Make a Reservation']"));
    }

    public void clickBackButton() {
        closeButton.click();
    }
    public void waitForTableCards() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath ("//div[@class='bg-white p-4 shadow-md rounded-lg flex flex-col md:flex-row h-full']")));
    }
    public boolean selectTableAndClickTimeslot(int requiredCapacity, String desiredTime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='bg-white p-4 shadow-md rounded-lg flex flex-col md:flex-row h-full']")));

        for (WebElement card : tableCards) {
            String cardText = card.getText().toLowerCase();
            if (cardText.contains(requiredCapacity + " people")) {
                try {
                    WebElement timeslotButton = card.findElement(
                            By.xpath(".//button[contains(text(), '" + desiredTime + "')]"));
                    if (timeslotButton.isEnabled()) {
                        timeslotButton.click();
                        return true;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Timeslot '" + desiredTime + "' not found in table with " + requiredCapacity + " people.");
                }
            }
        }

        return false;
    }
    public boolean selectTableByGuestCount(int requiredCapacity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        tableCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='bg-white p-4 shadow-md rounded-lg flex flex-col md:flex-row h-full']")));

        for (WebElement card : tableCards) {
            String cardText = card.getText().toLowerCase();


            if (cardText.contains(requiredCapacity + " people")) {
                try {
                    setGuestCount(requiredCapacity);
                    makeReservation();
                    return true;
                } catch (Exception e) {
                    System.out.println("Error while setting guest count or making reservation: " + e.getMessage());
                }
            }
        }

        System.out.println("No table found with capacity for " + requiredCapacity + " people.");
        return false;
    }

}
