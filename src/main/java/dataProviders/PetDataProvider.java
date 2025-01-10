package dataProviders;

import org.testng.annotations.DataProvider;
import models.Pet;
import models.Pet.Category;
import models.Pet.Tag;

import java.util.Arrays;
import java.util.List;

public class PetDataProvider {

    @DataProvider(name = "createPetData")
    public Object[][] provideCreatePetData() {
        return new Object[][]{
                {
                        new Pet(
                                101,
                                new Category(1, "Dog Pets"),
                                "doggie",
                                List.of("https://example.com/photo.jpg"),
                                List.of(new Tag(3, "Just")),
                                "available"
                        )
                },
                {
                        new Pet(
                                102,
                                new Category(2, "Cat Pets"),
                                "kitty",
                                List.of("https://example.com/cat_photo.jpg"),
                                List.of(new Tag(4, "Just")),
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
                                List.of("https://example.com/updated_dog_photo.jpg"),
                                List.of(new Tag(5, "Updated")),
                                "sold"
                        )
                },
                {
                        new Pet(
                                102,
                                new Category(2, "Cat Pets"),
                                "updatedKitty",
                                List.of("https://example.com/updated_cat_photo.jpg"),
                                List.of(new Tag(6, "Updated")),
                                "pending"
                        )
                }
        };
    }
}
