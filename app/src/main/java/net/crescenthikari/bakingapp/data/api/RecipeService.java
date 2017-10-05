package net.crescenthikari.bakingapp.data.api;

import net.crescenthikari.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Muhammad Fiqri Muthohar on 9/16/17.
 */

public interface RecipeService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<Response<List<Recipe>>> getRecipeList();
}
