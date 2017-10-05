package net.crescenthikari.bakingapp.features.list.model;

/**
 * Created by Muhammad Fiqri Muthohar on 9/18/17.
 */

public class Recipe {
    final private long id;

    final private String name;

    final private String image;

    final private int servings;

    public Recipe(long id, String name, String image, int servings) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.servings = servings;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getServings() {
        return servings;
    }
}
