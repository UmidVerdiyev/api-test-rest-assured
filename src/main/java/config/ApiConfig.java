package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiConfig {
    private static final String CONFIG_FILE = "src/test/resources/properties/config.properties";
    private static final Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file.", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url", "https://petstore.swagger.io/v2");
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout", "5000"));
    }

    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("read.timeout", "10000"));
    }

    public static int getMaxRetries() {
        return Integer.parseInt(properties.getProperty("max.retries", "3"));
    }
}

