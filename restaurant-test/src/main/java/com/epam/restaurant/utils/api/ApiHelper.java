package com.epam.restaurant.utils.api;

import com.epam.restaurant.pojos.AuthenticationPojo;
import com.epam.restaurant.pojos.CustomerTableReservationPojo;
import com.epam.restaurant.pojos.SigninPojo;
import com.epam.restaurant.config.ConfigurationReader;
import com.epam.restaurant.config.RestAssuredConfig;
import com.epam.restaurant.pojos.WaiterTableReservationPojo;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiHelper {
    private static final String AUTH_ENDPOINT = ConfigurationReader.getInstance().getProperty("AUTH_ENDPOINT");
    private static final String SIGN_IN_ENDPOINT = ConfigurationReader.getInstance().getProperty("SIGN_IN_ENDPOINT");
    private static final String LOCATIONS_ENDPOINT = ConfigurationReader.getInstance().getProperty("LOCATIONS_ENDPOINT");
    private static final String SPECIALITY_DISHES_ENDPOINT = ConfigurationReader.getInstance().getProperty("SPECIALITY_DISHES_ENDPOINT");
    private static final String POPULAR_DISHES_ENDPOINT = ConfigurationReader.getInstance().getProperty("POPULAR_DISHES_ENDPOINT");
    private static final String FEEDBACK_ENDPOINT = ConfigurationReader.getInstance().getProperty("FEEDBACK_ENDPOINT");
    private static final String LOCATION_SELECT_ENDPOINT = ConfigurationReader.getInstance().getProperty("LOCATION_SELECT_ENDPOINT");
    private static final String VIEW_TABLES_ENDPOINT = ConfigurationReader.getInstance().getProperty("VIEW_TABLES_ENDPOINT");
    private static final String VIEW_RESERVATIONS_ENDPOINT = ConfigurationReader.getInstance().getProperty("VIEW_RESERVATIONS_ENDPOINT");
    private static final String RESERVATION_ID_ENDPOINT = ConfigurationReader.getInstance().getProperty("RESERVATION_ID_ENDPOINT");
    private static final String BOOKINGS_CLIENT_ENDPOINT = ConfigurationReader.getInstance().getProperty("BOOKINGS_CLIENT_ENDPOINT");
    private static final String BOOKINGS_WAITER_ENDPOINT = ConfigurationReader.getInstance().getProperty("BOOKINGS_WAITER_ENDPOINT");
    private static final String DISHES_ENDPOINT = ConfigurationReader.getInstance().getProperty("DISHES_ENDPOINT");
    private static final String DISHES_ID_ENDPOINT = ConfigurationReader.getInstance().getProperty("DISHES_ID_ENDPOINT");

    public static Response registerUser(AuthenticationPojo user) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .body(user)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response SigninUser(SigninPojo user) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .body(user)
                .when()
                .post(SIGN_IN_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getLocations() {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .when()
                .get(LOCATIONS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getSpecialityDishes(String locationId) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .pathParam("id", locationId)
                .header("Accept", "application/json")
                .when()
                .get(SPECIALITY_DISHES_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getPopularDishes() {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .when()
                .get(POPULAR_DISHES_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getLocationFeedback(String locationId, String feedbackType, String sortBy) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .pathParam("id", locationId)
                .queryParam("type", feedbackType)
                .queryParam("sortBy", sortBy)
                .header("Accept", "application/json")
                .when()
                .get(FEEDBACK_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getShortLocationDetails() {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .when()
                .get(LOCATION_SELECT_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }


    public static Response getAvailableTables(String locationId, String date, String time, int guests) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .queryParam("locationId", locationId)
                .queryParam("date", date)
                .queryParam("time", time)
                .queryParam("guests", guests)
                .when()
                .get(VIEW_TABLES_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }


    public static Response getAvailableTablesWithoutTime(String locationId, String date, int guests) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .queryParam("locationId", locationId)
                .queryParam("date", date)
                .queryParam("guests", guests)
                .when()
                .get(VIEW_TABLES_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getAvailableTablesWithMissingParams(Map<String, Object> params) {

        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .queryParams(params != null ? params : Map.of())
                .when()
                .get(VIEW_TABLES_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }


    public static String loginAndGetAuthToken(String email, String password) {
        Response response = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Accept", "application/json")
                .body(Map.of("email", email, "password", password))
                .when()
                .post(SIGN_IN_ENDPOINT);

        System.out.println("STATUS: " + response.getStatusCode());
        System.out.println("BODY: " + response.getBody().asString());

        response.then().statusCode(200);

        String token = response.jsonPath().getString("accessToken");

        System.out.println("Generated auth token: " + token);

        Assert.assertNotNull("Token is null or empty!", token);

        return token;
    }



    public static Response getReservations(String authToken) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(VIEW_RESERVATIONS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }


    public static Response getReservationsWithNullToken() {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "null")
                .when()
                .get(VIEW_RESERVATIONS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }


    public static Response deleteReservation(String reservationId, String authToken) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .pathParam("id", reservationId)
                .header("Authorization", "Bearer " + authToken)
                .header("Accept", "application/json")
                .when()
                .delete(RESERVATION_ID_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response deleteReservationWithNullAuthToken(String reservationId) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .pathParam("id", reservationId)
                .header("Authorization", "null")
                .header("Accept", "application/json")
                .when()
                .delete(RESERVATION_ID_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
    public static Response customerBookTable(CustomerTableReservationPojo reservation, String authToken) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", authToken != null ? "Bearer " + authToken : "null")
                .header("Content-Type", "application/json")
                .body(reservation)
                .when()
                .post(BOOKINGS_CLIENT_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
    public static Response waiterBookTable(WaiterTableReservationPojo reservation, String authToken) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", authToken != null ? "Bearer " + authToken : "null")
                .header("Content-Type", "application/json")
                .body(reservation)
                .when()
                .post(BOOKINGS_WAITER_ENDPOINT);
    }
    public static Response getDishesByType(String dishType, String token) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("dishType", dishType)
                .header("Accept", "application/json")
                .when()
                .get(DISHES_ENDPOINT);
    }

    public static Response getDishesWithSorting(String dishType, String sortColumn, String sortOrder, String token) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("dishType", dishType)
                .queryParam("sort", sortColumn + "," + sortOrder)
                .header("Accept", "application/json")
                .when()
                .get(DISHES_ENDPOINT);
    }

    public static Response getDishesInvalidSort(String dishType, String invalidSortParam, String token) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("dishType", dishType)
                .queryParam("sort", invalidSortParam)
                .header("Accept", "application/json")
                .when()
                .get(DISHES_ENDPOINT);
    }

    public static Response getDishById(int dishId, String token) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + token)
                .pathParam("id", dishId)
                .header("Accept", "application/json")
                .when()
                .get(DISHES_ID_ENDPOINT);
    }

    public static Response getDishesWithMissingId(String token) {
        return given()
                .spec(RestAssuredConfig.getRequestSpec())
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .when()
                .get(DISHES_ENDPOINT);
    }
}



