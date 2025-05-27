package com.epam.restaurant.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfilePage {
    private WebDriver driver;

    @FindBy(xpath="//span[@class='font-semibold']")
    public WebElement roleElement;

    @FindBy(xpath="//p[@class='font-semibold text-lg']")
    public WebElement nameElement;

    @FindBy(xpath = "//div[contains(@class, 'relative') and contains(@class, 'ml-4')]//svg")
    public WebElement menuToggleButton;

    public ProfilePage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public String getRoleFromProfile()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(roleElement));
        return roleElement.getText();
    }
    public String getUserName()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(nameElement));
        return nameElement.getText().trim();
    }
    public void clickLogoutButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(menuToggleButton)).click();
        WebElement signOut = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Sign Out']")
        ));
        signOut.click();
    }
    public boolean isProfilePageLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(roleElement)).isDisplayed()
                && wait.until(ExpectedConditions.visibilityOf(nameElement)).isDisplayed();
    }
}
