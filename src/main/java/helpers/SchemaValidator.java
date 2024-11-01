package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

import java.io.File;
import java.io.IOException;

public class SchemaValidator {
    private static final JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static boolean validate(JsonNode jsonResponse, String schemaFileName) {
        try {
            File schemaFile = new File("src/test/resources/schemas/" + schemaFileName);
            JsonNode schemaNode = objectMapper.readTree(schemaFile);

            ProcessingReport report = validator.validate(schemaNode, jsonResponse);
            return report.isSuccess();
        } catch (IOException | ProcessingException e) {
            throw new RuntimeException("Failed to validate JSON schema for file: " + schemaFileName, e);
        }
    }

    public static boolean validate(String jsonResponse, String schemaFileName) {
        try {
            JsonNode jsonResponseNode = objectMapper.readTree(jsonResponse);
            return validate(jsonResponseNode, schemaFileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON response for validation.", e);
        }
    }
}

