package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import exceptions.ApiException;

import java.io.File;
import java.io.IOException;

public class PetService {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Response createPet(Pet pet) {
        try {
            String petJson = objectMapper.writeValueAsString(pet);
            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType("application/json")
                    .body(petJson)
                    .post("/pet");

            if (response.getStatusCode() != 200) {
                throw new ApiException(response.getStatusCode(), "Failed to create pet: " + response.getStatusLine());
            }
            return response;
        } catch (IOException e) {
            throw new ApiException("Failed to serialize pet object", e);
        }
    }


    public Response getPetById(int petId) {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .pathParam("petId", petId)
                .get("/pet/{petId}");

        if (response.getStatusCode() != 200 && response.getStatusCode() != 404) {
            throw new ApiException(response.getStatusCode(), "Unexpected status while retrieving pet with ID: " + petId);
        }

        return response;
    }


    public Response updatePet(Pet pet) {
        try {
            String petJson = objectMapper.writeValueAsString(pet);
            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType("application/json")
                    .body(petJson)
                    .put("/pet");

            return response;
        } catch (IOException e) {
            throw new ApiException("Failed to serialize pet object", e);
        }
    }


    public Response deletePet(int petId) {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.URLENC)
                .pathParam("petId", petId)
                .delete("/pet/{petId}");


        if (response.getStatusCode() != 200 && response.getStatusCode() != 404) {
            throw new ApiException(response.getStatusCode(), "Failed to delete pet with ID: " + petId);
        }

        return response;
    }

    public Response findPetsByStatus(String status) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .queryParam("status", status)
                .get("/pet/findByStatus");
    }

    public Response uploadPetPhoto(int petId, File file) {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("multipart/form-data")
                .pathParam("petId", petId)
                .formParam("additionalMetadata", "Meta Data")
                .multiPart("file", file)
                .post("/pet/{petId}/uploadImage");

        if (response.getStatusCode() != 200) {
            throw new ApiException(response.getStatusCode(), "Failed to upload photo for pet ID: " + petId);
        }
        return response;
    }

    public Response updatePetWithFormData(int petId, String name, String status) {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", petId)
                .formParam("name", name)
                .formParam("status", status)
                .post("/pet/{petId}");

        if (response.getStatusCode() != 200) {
            throw new ApiException(response.getStatusCode(), "Failed to update pet with form data for pet ID: " + petId);
        }
        return response;
    }


}
