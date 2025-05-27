package com.epam.restaurant.utils.ui;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    private static Set<Cookie> savedCookies = new HashSet<>();

    public static void saveSessionCookies(WebDriver driver) {
        savedCookies = driver.manage().getCookies();
        System.out.println("Cookies saved: " + savedCookies.size());
    }

    public static void restoreSessionCookies(WebDriver driver) {
        if (savedCookies == null || savedCookies.isEmpty()) {
            System.out.println("No cookies to restore!");
            return;
        }

        for (Cookie cookie : savedCookies) {
            driver.manage().addCookie(cookie);
        }

        System.out.println("Cookies restored: " + savedCookies.size());
    }
}