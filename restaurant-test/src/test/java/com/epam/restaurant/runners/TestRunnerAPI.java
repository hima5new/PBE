package com.epam.restaurant.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {
                  "src/test/resources/features/api"
        },
        glue = {"com.epam.restaurant.stepdefinitions.api", "com.epam.restaurant.hooks.api"},
        plugin = {"pretty" ,  "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        tags="@api"
)
public class TestRunnerAPI extends AbstractTestNGCucumberTests {
}
