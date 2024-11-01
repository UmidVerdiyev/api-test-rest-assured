package utils;

import io.restassured.response.Response;
import models.Pet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ApiUtil {
    private static final Logger logger = LogManager.getLogger(ApiUtil.class);

    public static void logResponseDetails(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Headers: {}", response.getHeaders());
        logger.info("Response Body: {}", response.getBody().asPrettyString());
    }

    public static void logRequestDetails(String method, String uri, Object body) {
        logger.info("Request Method: {}", method);
        logger.info("Request URI: {}", uri);
        if (body != null) {
            logger.info("Request Body: {}", body);
        }
    }

    public static boolean waitForCondition(Supplier<Boolean> condition, int timeoutInSeconds) {
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutInSeconds);
        while (System.currentTimeMillis() < endTime) {
            if (condition.get()) {
                return true;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
        return false;
    }

    public static void delay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Delay interrupted");
        }
    }

}
