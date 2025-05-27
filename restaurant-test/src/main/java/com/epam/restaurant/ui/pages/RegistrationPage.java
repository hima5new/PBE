package com.epam.restaurant.ui.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegistrationPage {
    private WebDriver driver;

    @FindBy(id="firstName")
    WebElement firstNameField;

    @FindBy(id = "lastName")
    WebElement lastNameField;

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "confirmPassword")
    WebElement confirmPasswordField;


    @FindBy(xpath = "//button[@type='submit']")
    WebElement registerButton;

     @FindBy(xpath="//li[@class='text-red-500' and text()='● At least one special character required']")
     WebElement passwordError;

    @FindBy(xpath = "//p[@class='text-red-500 text-xs mt-1']")
    WebElement emailErrorMessage;


    @FindBy(xpath = "//p[contains(@class, 'text-red') and text()='First name can be up to 50 characters and only contain Latin letters, hyphens, and apostrophes are allowed.']")
    WebElement firstNameErrorMessage;

    @FindBy(xpath="//p[contains(text(),'Passwords do not match')]")
    WebElement confirmPasswordErrorMessage;


    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public void enterFirstName(String firstName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {

        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {

        emailField.sendKeys(email);
    }
    public void enterPassword(String password) {

        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {

        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void clickRegister() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(registerButton));
        registerButton.click();
    }


    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement successNotation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
        return successNotation.getText();
    }


    public String getErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement errorNotation =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
        return errorNotation.getText();
    }


    public String getEmailErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(emailErrorMessage));
        return emailErrorMessage.getText();
    }


    public String getNameErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(firstNameErrorMessage));
        return firstNameErrorMessage.getText();
    }
    public boolean isUserOnRegistrationPage()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
         return firstNameField.isDisplayed();
    }

    public String getPasswordError()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(passwordError));
        return passwordError.getText();
    }

    public String getConfirmPasswordErrorMessage()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(confirmPasswordErrorMessage));
        return confirmPasswordErrorMessage.getText();
    }
}