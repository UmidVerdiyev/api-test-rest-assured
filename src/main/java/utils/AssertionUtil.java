package utils;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

public class AssertionUtil {


    public static void assertStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
    }

    public static void assertResponseTime(Response response, long maxResponseTime) {
        long responseTime = response.getTime();
        Assert.assertTrue(responseTime <= maxResponseTime,
                "Expected response time <= " + maxResponseTime + "ms but got " + responseTime + "ms");
    }

    public static void assertFieldEquals(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actualValue, expectedValue,
                "Expected value at '" + jsonPath + "' to be '" + expectedValue + "' but got '" + actualValue + "'");
    }

    public static void assertFieldExists(Response response, String jsonPath) {
        Object fieldValue = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(fieldValue, "Expected field at '" + jsonPath + "' to exist, but it was null");
    }

    public static void assertBodyContains(Response response, String expectedSubstring) {
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains(expectedSubstring),
                "Expected response body to contain '" + expectedSubstring + "', but it did not.");
    }
    public static void assertFieldSize(Response response, String jsonPath, int expectedSize) {
        List<?> fieldList = response.jsonPath().getList(jsonPath);

        if (fieldList == null) {
            Assert.assertEquals(0, expectedSize,
                    "Expected size of field at '" + jsonPath + "' to be '" + expectedSize + "', but the field was missing.");
        } else {
            int actualSize = fieldList.size();
            Assert.assertEquals(actualSize, expectedSize,
                    "Expected size of field at '" + jsonPath + "' to be '" + expectedSize + "' but got '" + actualSize + "'");
        }
    }


}

