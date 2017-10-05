package net.crescenthikari.bakingapp.data.api.model;

import com.google.gson.annotations.SerializedName;

import net.crescenthikari.bakingapp.data.model.Recipe;

import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 9/16/17.
 */

public class RecipeListResponse {
    @SerializedName("")
    public List<Recipe> recipes;
}
