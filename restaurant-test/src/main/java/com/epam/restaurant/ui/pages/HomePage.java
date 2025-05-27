package com.epam.restaurant.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;

    private static final int DEFAULT_TIMEOUT = 15;

    @FindBy(xpath = "//a[text()='Reservation']")
    private WebElement homePageElement;

    @FindBy(xpath = "//a[contains(text(),'Book a Table')]")
    private WebElement homePageBook;

    @FindBy(xpath = "//div[@role='alert']")
    private WebElement welcomeMessage;

    @FindBy(xpath = "//div[@class='relative ml-4']")
    private WebElement profile;

    @FindBy(xpath = "//button[contains(text(),'Sign Out')]")
    private WebElement signOutButton;

    @FindBy(xpath = "//a[text()='My Profile']")
    private WebElement profileLink;

    @FindBy(xpath="//p[contains(text(),'Green & Tasty')]")
    private WebElement pageTitle;

    @FindBy(xpath="//a[text()='Book a Table']")
    private WebElement bookTableLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public boolean isUserOnHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOf(homePageElement));
        return homePageElement.isDisplayed();
    }


    public boolean isVisitorOnHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOf(homePageBook));
        return homePageBook.isDisplayed();
    }


    public String getWelcomeMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        return welcomeMessage.getText();
    }


    public void clickLogoutButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.elementToBeClickable(profile)).click();
        wait.until(ExpectedConditions.elementToBeClickable(signOutButton)).click();
    }


    public ProfilePage clickProfileLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        try {
            WebElement toastCloseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='Toastify__toast--success']//button[@aria-label='close']")));
            toastCloseButton.click();
        } catch (Exception e) {
            System.out.println("Toast notification not found or already closed.");
        }
        wait.until(ExpectedConditions.elementToBeClickable(profile)).click();
        wait.until(ExpectedConditions.elementToBeClickable(profileLink));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", profileLink);
        profileLink.click();

        return new ProfilePage(driver);
    }
    public ProfilePage navigateToProfile()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        try {
            WebElement toastCloseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='Toastify__toast--success']//button[@aria-label='close']")));
            toastCloseButton.click();
        } catch (Exception e) {
            System.out.println("Toast notification not found or already closed.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(profile)).click();
        wait.until(ExpectedConditions.elementToBeClickable(profileLink));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", profileLink);
        profileLink.click();

        return new ProfilePage(driver);
    }
    public BookTablePage navigateToBookTable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        try {

            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Toastify__toast-container")));
            WebElement toastCloseButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'Toastify__toast--success')]//button[@aria-label='close']")));
            toastCloseButton.click();
            System.out.println("Closed the toast notification.");
        } catch (TimeoutException e) {
            System.out.println("Toast notification not shown.");
        } catch (Exception e) {
            System.out.println("Unexpected error closing toast: " + e.getMessage());
        }

        try {

            wait.until(ExpectedConditions.elementToBeClickable(bookTableLink));
            bookTableLink.click();
            System.out.println("Clicked on Book a Table link.");
        } catch (StaleElementReferenceException sere) {

            bookTableLink = driver.findElement(By.xpath("//a[text()='Book a Table']"));
            bookTableLink.click();
        } catch (Exception e) {
            System.out.println("Error clicking Book a Table link: " + e.getMessage());
            throw e;
        }

        BookTablePage bookTablePage = new BookTablePage(driver);
        if (!bookTablePage.isUserOnBookATable()) {
            throw new IllegalStateException("Book Table page not loaded correctly.");
        }

        return bookTablePage;
    }

    public String getTitle()
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText();

    }
}
