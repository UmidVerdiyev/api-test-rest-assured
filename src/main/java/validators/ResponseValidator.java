package validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.AssertionUtil;

import java.io.File;
import java.io.IOException;

public class ResponseValidator {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void validateSchema(Response response, String schemaFileName) {
        try {

            File schemaFile = new File("src/test/resources/schemas/" + schemaFileName);
            JsonNode schemaNode = objectMapper.readTree(schemaFile);
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            JsonNode responseNode = objectMapper.readTree(response.asString());
            ProcessingReport report = schema.validate(responseNode);

            Assert.assertTrue(report.isSuccess(), "JSON schema validation failed: " + report.toString());
        } catch (IOException | ProcessingException e) {
            throw new RuntimeException("Schema validation error for file: " + schemaFileName, e);
        }
    }

    public static void validateField(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actualValue, expectedValue,
                "Expected value for '" + jsonPath + "' is '" + expectedValue + "' but got '" + actualValue + "'");
    }

    public static void validateFieldExists(Response response, String jsonPath) {
        AssertionUtil.assertFieldExists(response, jsonPath);
    }
}
