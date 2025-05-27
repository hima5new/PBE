package com.epam.restaurant.util.ui;

import java.util.Arrays;
import java.util.List;

public class EmailListMock {

    private static final List<String> waiterEmailList = Arrays.asList(
            "peter.parker@restaurant.com",
            "john.doe@restaurant.com",
            "tony.stark@restaurant.com",
            "captian@restaurant.com"
    );

    private static final List<String> customerEmailList = Arrays.asList(
            "peter.parker@restaurant.com",
            "james.wilson@outlook.com",
            "vaishnavi_srividya2603@gmail.com"
    );

    public static boolean isEmailInWaiterList(String email) {
        return waiterEmailList.contains(email);
    }

    public static boolean isEmailInCustomerList(String email) {
        return customerEmailList.contains(email);
    }

    public static String determineRole(String email) {
        if (isEmailInWaiterList(email)) {
            return "Waiter";
        } else if (isEmailInCustomerList(email)) {
            return "Customer";
        }
        return "Visitor";
    }
}