package net.crescenthikari.bakingapp.features.widget.data.model;

/**
 * Created by Muhammad Fiqri Muthohar on 9/29/17.
 */

public class RecipeWidgetOption {
    private final int recipeId;
    private final String recipeName;

    public RecipeWidgetOption(int recipeId, String recipeName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }
}
