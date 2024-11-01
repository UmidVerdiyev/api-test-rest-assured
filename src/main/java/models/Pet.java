package models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Pet {

    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private String status;

    public Pet() {}

    public Pet(int id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }

    @Setter
    @Getter
    public static class Category {
        private int id;
        private String name;

        public Category() {}

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }

    }


    @Setter
    @Getter
    public static class Tag {
        private int id;
        private String name;

        public Tag() {}

        public Tag(int id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}
