package tests;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import models.Pet;
import models.Pet.Category;
import models.Pet.Tag;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import reporters.ExtentReportManager;
import services.PetService;
import utils.ApiUtil;
import utils.AssertionUtil;
import validators.ResponseValidator;
import java.io.File;
import java.util.List;


public class PetCRUDTest {

    private PetService petService;
    private int petId;
    private final File file = new File("src/main/resources/images/sampleImage.png");

    @BeforeClass
    public void setup() {
        petService = new PetService();
        ExtentReportManager.startTest("Pet CRUD Operations", "Tests CRUD operations for Pets API");
    }

    @Test(description = "Positive - Create Pet")
    @DisplayName("For creating pets with some Data")
    public void createPet() {
        Pet pet = new Pet(
                101,
                new Category(1, "Dog Pets"),
                "doggie",
                List.of("https://example.com/photo.jpg"),
                List.of(new Tag(3, "Just")),
                "available"
        );

        Response response = petService.createPet(pet);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Created pet with name: " + pet.getName());
        ResponseValidator.validateSchema(response, "PetSchema.json");
        AssertionUtil.assertStatusCode(response, 200);
        petId = response.then().extract().path("id");
        Assert.assertTrue(petId > 0, "Pet ID should be generated.");
    }

    @Test(description = "Positive - Find Pet By Status", dependsOnMethods = "createPet")
    public void findPetByStatus() {
        Pet pet = new Pet();
        Assert.assertNotNull(petId, "petId is not set. Make sure to run createPet first.");

        int retries = 3;
        while (retries > 0) {
            Response response = petService.findPetsByStatus("available");
            boolean petFound = response.jsonPath().getList("id").contains(petId);
            ApiUtil.logResponseDetails(response);
            ExtentReportManager.getTest().info("Find Pet By Status " + pet.getName());

            if (petFound) {
                AssertionUtil.assertStatusCode(response, 200);
                AssertionUtil.assertFieldEquals(response, "find { it.id == " + petId + " }.name", "doggie");
                AssertionUtil.assertFieldEquals(response, "find { it.id == " + petId + " }.status", "available");
                break;
            } else {
                retries--;
                if (retries == 0) {
                    Assert.fail("Pet ID " + petId + " not found after retries.");
                }
                ApiUtil.delay(2);
            }
        }
    }

    @Test(description = "Positive - Update Pet", dependsOnMethods = "findPetByStatus")
    public void updatePet() {
        Pet pet =  petService.getPetById(petId).as(Pet.class);
        pet.setName("updatedDoggie");
        pet.setStatus("sold");

        Response response = petService.updatePet(pet);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Update Pet: " + pet.getName());

        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "name", "updatedDoggie");
        AssertionUtil.assertFieldEquals(response, "status", "sold");
    }

    @Test(description = "Positive - Delete Pet by ID", dependsOnMethods = "updatePet")
    public void deletePet() {
        Pet pet = new Pet();
        Response response = petService.deletePet(petId);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("UDelete Pet by ID: " + pet.getName());
        AssertionUtil.assertStatusCode(response, 200);


        Response getResponse =  petService.getPetById(petId);
        AssertionUtil.assertStatusCode(getResponse, 404);
    }

    @Test(description = "Positive - Upload Pet Photo", dependsOnMethods = "createPet")
    public void uploadPetPhoto() {
        Pet pet = new Pet();
        Response response = petService.uploadPetPhoto(petId, file);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Upload Pet Photo: " + pet.getName());
        AssertionUtil.assertStatusCode(response, 200);  // Verify status code
    }



    @Test(description = "Negative - Upload Photo with Invalid ID")
    public void uploadPhotoInvalidId() {
        Pet pet = new Pet();
        Response response = petService.uploadPetPhoto(999999, file);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Upload Photo with Invalid ID: " + pet.getName());

        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "code", 200);
        AssertionUtil.assertFieldEquals(response, "type", "unknown");
        AssertionUtil.assertFieldEquals(response, "message",
                "additionalMetadata: Meta Data\nFile uploaded to ./" + file.getName() + ", " + file.length() + " bytes");
    }

    @Test(description = "Negative - Update Pet with Missing Fields")
    public void updatePetWithMissingFields() {
        Pet incompletePet = new Pet();
        Response response = petService.updatePet(incompletePet);

        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Update Pet with Missing Fields: " + incompletePet.getName());


        AssertionUtil.assertStatusCode(response, 200);

        AssertionUtil.assertFieldSize(response, "photoUrls", 0);
        AssertionUtil.assertFieldSize(response, "tags", 0);
    }



    @Test(description = "Negative - Find Pet by Invalid Status")
    public void findPetByInvalidStatus() {
        Pet pet = new Pet();
        Response response = petService.findPetsByStatus("nonexistent");
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Find Pet by Invalid Status: " + pet.getName());
        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "size()", 0);
    }

    @Test(description = "Positive - Find Pet By ID", dependsOnMethods = "createPet")
    public void findPetById() {
        Pet pet = new Pet();
        Response response = petService.getPetById(petId);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Find Pet By ID: " + pet.getName());


        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "id", petId);
    }

    @Test(description = "Negative - Find Pet By Nonexistent ID")
    public void findPetByNonexistentId() {
        Pet pet = new Pet();
        Response response = petService.getPetById(999999);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Find Pet By Nonexistent ID: " + pet.getName());


        AssertionUtil.assertStatusCode(response, 404);
    }



    @Test(description = "Positive - Update Pet with Form Data", dependsOnMethods = "createPet")
    public void updatePetWithFormData() {
        Pet pet = new Pet();
        Response response = petService.updatePetWithFormData(petId, "Updated Dog", "available");
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Update Pet with Form Data: " + pet.getName());

        AssertionUtil.assertStatusCode(response, 200);
    }

    @Test(description = "Negative - Update Pet with Invalid Form Data", dependsOnMethods = "createPet")
    public void updatePetWithInvalidFormData() {
        Pet pet = new Pet();
        Response response = petService.updatePetWithFormData(petId, "", "unknownstatus");
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Update Pet with Invalid Form Data: " + pet.getName());


        AssertionUtil.assertStatusCode(response, 200);

        AssertionUtil.assertFieldEquals(response, "code", 200);
        AssertionUtil.assertFieldEquals(response, "message", String.valueOf(petId));
    }


    @Test(description = "Negative - Delete Nonexistent Pet")
    public void deleteNonexistentPet() {
        Pet pet = new Pet();
        Response response = petService.deletePet(99999);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Delete Nonexistent Pet: " + pet.getName());

        AssertionUtil.assertStatusCode(response, 404);
    }


    @AfterSuite
    public void tearDown() {
        ExtentReportManager.flush();
    }
}
