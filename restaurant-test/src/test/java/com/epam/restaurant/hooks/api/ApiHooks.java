package com.epam.restaurant.hooks.api;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import java.io.ByteArrayInputStream;

public class ApiHooks {

    private static Response lastResponse;
    @Before("@api")
    public void setup() {
        System.out.println("Setting up test environment...");
    }

    @After("@api")
    public void teardown(Scenario scenario) {
        if (lastResponse != null) {
            Allure.addAttachment("Response Body", new ByteArrayInputStream(lastResponse.getBody().asString().getBytes()));
        }

        if (scenario.isFailed()) {
            System.out.println("Test failed: " + scenario.getName());
        }
    }
    public static void setLastResponse(Response response) {
        lastResponse = response;
    }
}


