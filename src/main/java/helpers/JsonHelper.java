package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final String JSON_FILE_PATH = "src/test/resources/testData/apiData.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static JsonNode loadJson() throws IOException {
        return objectMapper.readTree(new File(JSON_FILE_PATH));
    }

    public static String getRequestBody(String key) {
        try {
            JsonNode jsonNode = loadJson().get(key);
            return jsonNode != null ? jsonNode.toString() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON request body for key: " + key, e);
        }
    }
}
