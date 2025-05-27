package com.epam.restaurant.hooks.ui;


import com.epam.restaurant.utils.ui.DriverFactory;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.slf4j.Logger;
import com.epam.restaurant.utils.ui.LoggerUtils;

public class HooksUI {
    private static final Logger logger = LoggerUtils.getLogger(HooksUI.class);

    @Before("@ui")
    public void setUp() {
        logger.info("Initializing the WebDriver for the test.");
        String browser = System.getProperty("browser", "chrome");
        DriverFactory.initDriver(browser);
    }

    @After("@ui")
    public void tearDown() {
        logger.info("Tearing down the WebDriver and closing the browser.");
        DriverFactory.quitDriver();
    }
}