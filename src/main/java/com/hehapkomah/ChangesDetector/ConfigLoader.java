package com.hehapkomah.ChangesDetector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties = new Properties();

    public ConfigLoader(String configFileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Sorry, unable to find " + configFileName);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration file", ex);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
