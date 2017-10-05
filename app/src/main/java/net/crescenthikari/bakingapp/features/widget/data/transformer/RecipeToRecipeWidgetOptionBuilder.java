package net.crescenthikari.bakingapp.features.widget.data.transformer;

import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.features.widget.data.model.RecipeWidgetOption;

/**
 * Created by Muhammad Fiqri Muthohar on 9/29/17.
 */

public class RecipeToRecipeWidgetOptionBuilder implements RecipeWidgetOptionBuilder<Recipe> {
    int recipeId;
    String recipeName;

    @Override
    public RecipeWidgetOptionBuilder fromData(Recipe data) {
        recipeId = data.getId();
        recipeName = data.getName();
        return this;
    }

    @Override
    public RecipeWidgetOption build() {
        return new RecipeWidgetOption(recipeId, recipeName);
    }
}
