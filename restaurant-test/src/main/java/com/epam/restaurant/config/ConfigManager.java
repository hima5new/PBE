package com.epam.restaurant.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private Properties properties;

    public static class ConfigException extends RuntimeException {
        public ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public ConfigManager() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new ConfigException("Failed to load config file.", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}