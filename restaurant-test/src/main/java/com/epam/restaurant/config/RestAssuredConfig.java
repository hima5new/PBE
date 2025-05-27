package com.epam.restaurant.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;


public class RestAssuredConfig {
    public static RequestSpecification getRequestSpec() {
        String baseUri = ConfigurationReader.getInstance().getProperty("BASE_URI");
        System.out.println("Resolved Base URI: " + baseUri);
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType("application/json")
                .build();
    }
}
