package com.epam.restaurant.utils.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {
    private static WebDriver driver;


    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();


    public static WebDriver initDriver(String browser) {
        if (browser == null || browser.isEmpty()) {
            throw new RuntimeException("Browser name is null or empty. Please provide a valid browser name.");
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                threadLocalDriver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--width=1920", "--height=1080");
                threadLocalDriver.set(new FirefoxDriver(firefoxOptions));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                threadLocalDriver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new RuntimeException("Invalid browser name: " + browser + ". Supported browsers are chrome, firefox, and edge.");
        }


        driver = threadLocalDriver.get();
        return driver;
    }


    public static WebDriver getDriver() {
        if (threadLocalDriver.get() == null) {
            throw new RuntimeException(
                    "WebDriver is not initialized. Make sure to invoke DriverFactory.initDriver(browser) before calling getDriver()."
            );
        }
        return threadLocalDriver.get();
    }


    public static void quitDriver() {
        if (threadLocalDriver.get() != null) {
            threadLocalDriver.get().quit();
            threadLocalDriver.remove();
        }
    }
}