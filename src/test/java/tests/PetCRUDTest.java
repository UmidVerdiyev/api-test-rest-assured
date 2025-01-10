package tests;

import io.restassured.response.Response;
import models.Pet;
import models.Pet.Category;
import models.Pet.Tag;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporters.ExtentReportManager;
import services.PetService;
import utils.ApiUtil;
import utils.AssertionUtil;
import validators.ResponseValidator;

import java.io.File;
import java.lang.reflect.Method;
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

    @BeforeMethod
    public void startTest(Method method) {
        ExtentReportManager.startTest(method.getName(), "Description: " + method.getAnnotation(Test.class).description());
    }


    @Test(description = "Positive - Create Pet")
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
        Assert.assertNotNull(petId, "petId is not set. Make sure to run createPet first.");

        int retries = 3;
        while (retries > 0) {
            Response response = petService.findPetsByStatus("available");
            ApiUtil.logResponseDetails(response);

            boolean petFound = response.jsonPath().getList("id").contains(petId);
            ExtentReportManager.getTest().info("Find Pet By Status: Attempt to find pet with ID " + petId);

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
        Response getResponse = petService.getPetById(petId);
        Pet pet = getResponse.as(Pet.class);
        pet.setName("updatedDoggie");
        pet.setStatus("sold");

        Response response = petService.updatePet(pet);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Update Pet: Updated name to " + pet.getName());

        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "name", "updatedDoggie");
        AssertionUtil.assertFieldEquals(response, "status", "sold");
    }

    @Test(description = "Positive - Delete Pet by ID", dependsOnMethods = "updatePet")
    public void deletePet() {
        Response response = petService.deletePet(petId);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Deleted Pet with ID: " + petId);
        AssertionUtil.assertStatusCode(response, 200);

        Response getResponse = petService.getPetById(petId);
        AssertionUtil.assertStatusCode(getResponse, 404);
    }

    @Test(description = "Positive - Upload Pet Photo", dependsOnMethods = "createPet")
    public void uploadPetPhoto() {
        Response response = petService.uploadPetPhoto(petId, file);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Uploaded photo for Pet ID: " + petId);
        AssertionUtil.assertStatusCode(response, 200);
    }

    @Test(description = "Negative - Upload Photo with Invalid ID")
    public void uploadPhotoInvalidId() {
        Response response = petService.uploadPetPhoto(999999, file);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Attempted to upload photo with invalid ID: 999999");

        AssertionUtil.assertStatusCode(response, 404);
    }

    @Test(description = "Negative - Update Pet with Missing Fields")
    public void updatePetWithMissingFields() {
        Pet incompletePet = new Pet();
        Response response = petService.updatePet(incompletePet);

        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Attempted to update pet with missing fields.");

        AssertionUtil.assertStatusCode(response, 400);
    }

    @Test(description = "Negative - Find Pet by Invalid Status")
    public void findPetByInvalidStatus() {
        Response response = petService.findPetsByStatus("nonexistent");
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Attempted to find pet with invalid status.");

        AssertionUtil.assertStatusCode(response, 404);
    }

    @Test(description = "Positive - Find Pet By ID", dependsOnMethods = "createPet")
    public void findPetById() {
        Response response = petService.getPetById(petId);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Found Pet by ID: " + petId);

        AssertionUtil.assertStatusCode(response, 200);
        AssertionUtil.assertFieldEquals(response, "id", petId);
    }

    @Test(description = "Negative - Delete Nonexistent Pet")
    public void deleteNonexistentPet() {
        Response response = petService.deletePet(99999);
        ApiUtil.logResponseDetails(response);
        ExtentReportManager.getTest().info("Attempted to delete nonexistent pet with ID: 99999");

        AssertionUtil.assertStatusCode(response, 404);
    }

    @AfterMethod
    public void logTestStatus(ITestResult result) {
        if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportManager.getTest().skip("Test skipped: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.FAILURE) {
            ExtentReportManager.getTest().fail("Test failed: " + result.getThrowable());
        }
    }


    @AfterSuite
    public void tearDown() {
        ExtentReportManager.flush();
    }
}
