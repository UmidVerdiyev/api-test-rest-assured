package dataProviders;

import org.testng.annotations.DataProvider;
import models.Pet;
import models.Pet.Category;
import models.Pet.Tag;

import java.util.Arrays;

public class PetDataProvider {

    @DataProvider(name = "createPetData")
    public Object[][] provideCreatePetData() {
        return new Object[][]{
                {
                        new Pet(
                                101,
                                new Category(1, "Dog Pets"),
                                "doggie",
                                Arrays.asList("https://example.com/photo.jpg"),
                                Arrays.asList(new Tag(3, "Just")),
                                "available"
                        )
                },
                {
                        new Pet(
                                102,
                                new Category(2, "Cat Pets"),
                                "kitty",
                                Arrays.asList("https://example.com/cat_photo.jpg"),
                                Arrays.asList(new Tag(4, "Just")),
                                "available"
                        )
                }
        };
    }

    @DataProvider(name = "updatePetData")
    public Object[][] provideUpdatePetData() {
        return new Object[][]{
                {
                        new Pet(
                                101,
                                new Category(1, "Dog Pets"),
                                "updatedDoggie",
                                Arrays.asList("https://example.com/updated_dog_photo.jpg"),
                                Arrays.asList(new Tag(5, "Updated")),
                                "sold"
                        )
                },
                {
                        new Pet(
                                102,
                                new Category(2, "Cat Pets"),
                                "updatedKitty",
                                Arrays.asList("https://example.com/updated_cat_photo.jpg"),
                                Arrays.asList(new Tag(6, "Updated")),
                                "pending"
                        )
                }
        };
    }
}
