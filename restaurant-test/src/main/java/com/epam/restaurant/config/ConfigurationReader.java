package com.epam.restaurant.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static ConfigurationReader instance;
    private Properties properties;

    private ConfigurationReader() {
        properties = new Properties();
        try (FileInputStream file = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file!", e);
        }
    }

    public static ConfigurationReader getInstance() {
        if (instance == null) {
            instance = new ConfigurationReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

