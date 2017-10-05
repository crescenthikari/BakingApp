package net.crescenthikari.bakingapp.data.repository;

import net.crescenthikari.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Muhammad Fiqri Muthohar on 9/16/17.
 */

public interface RecipeRepository {
    Observable<List<Recipe>> getRecipes();

    Observable<Recipe> getRecipe(long recipeId);
}
