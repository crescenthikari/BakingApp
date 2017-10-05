package net.crescenthikari.bakingapp.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.crescenthikari.bakingapp.data.api.RecipeService;
import net.crescenthikari.bakingapp.data.db.BakingAppProvider;
import net.crescenthikari.bakingapp.data.db.table.RecipeColumns;
import net.crescenthikari.bakingapp.data.model.IngredientsItem;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.model.StepsItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Muhammad Fiqri Muthohar on 9/16/17.
 */

public class RecipeRepositoryImpl implements RecipeRepository {

    private static Type ingredientsType;
    private static Type stepsType;

    static {
        ingredientsType = new TypeToken<List<IngredientsItem>>() {
        }.getType();
        stepsType = new TypeToken<List<StepsItem>>() {
        }.getType();
    }

    private final RecipeService recipeService;
    private final ContentResolver contentResolver;
    private final Gson gson;
    private List<Recipe> recipes;
    private Recipe recipe;

    public RecipeRepositoryImpl(RecipeService recipeService,
                                ContentResolver contentResolver) {
        this.recipeService = recipeService;
        this.contentResolver = contentResolver;
        gson = new Gson();
    }

    Observable<List<Recipe>> getRecipesFromMemory() {
        return Observable.create(new ObservableOnSubscribe<List<Recipe>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Recipe>> observableEmitter) throws Exception {
                if (recipes != null) {
                    observableEmitter.onNext(recipes);
                }
                observableEmitter.onComplete();
            }
        });
    }

    Observable<List<Recipe>> getRecipesFromDisk() {
        return Observable.create(new ObservableOnSubscribe<List<Recipe>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Recipe>> observableEmitter) throws Exception {
                Cursor cursor = contentResolver.query(
                        BakingAppProvider.Recipes.CONTENT_URI,
                        BakingAppProvider.Recipes.projections,
                        null,
                        null,
                        null
                );
                List<Recipe> recipeList = null;
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    recipeList = new ArrayList<>();
                    do {
                        recipeList.add(getRecipeFromCursor(cursor));
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                if (recipeList != null) {
                    observableEmitter.onNext(recipeList);
                }
                observableEmitter.onComplete();
            }
        }).doOnNext(new Consumer<List<Recipe>>() {
            @Override
            public void accept(List<Recipe> recipes) throws Exception {
                RecipeRepositoryImpl.this.recipes = recipes;
            }
        });
    }

    Observable<List<Recipe>> getRecipesFromApi() {
        return recipeService
                .getRecipeList()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Response<List<Recipe>>, ObservableSource<List<Recipe>>>() {
                    @Override
                    public ObservableSource<List<Recipe>> apply(@NonNull Response<List<Recipe>> listResponse) throws Exception {
                        if (listResponse != null && !listResponse.body().isEmpty()) {
                            for (Recipe recipe : listResponse.body()) {
                                contentResolver.insert(
                                        BakingAppProvider.Recipes.withId(recipe.getId()),
                                        getRecipeCV(recipe)
                                );
                            }
                        }
                        return Observable.just(listResponse.body());
                    }
                });
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return Observable
                .concat(getRecipesFromMemory(), getRecipesFromDisk(), getRecipesFromApi())
                .take(1)
                .subscribeOn(Schedulers.io())
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<Recipe> getRecipeFromMemory(final long recipeId) {
        return Observable.create(new ObservableOnSubscribe<Recipe>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Recipe> observableEmitter) throws Exception {
                if (recipe != null && recipe.getId() == recipeId) {
                    observableEmitter.onNext(recipe);
                }
                observableEmitter.onComplete();
            }
        });
    }

    Observable<Recipe> getRecipeFromDisk(final long recipeId) {
        return Observable.create(new ObservableOnSubscribe<Recipe>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Recipe> observableEmitter) throws Exception {
                Cursor cursor = contentResolver.query(
                        BakingAppProvider.Recipes.withId(recipeId),
                        BakingAppProvider.Recipes.projections,
                        null,
                        null,
                        null
                );
                Recipe result = null;
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    result = getRecipeFromCursor(cursor);
                    cursor.close();
                }
                if (result != null) {
                    observableEmitter.onNext(result);
                }
                observableEmitter.onComplete();
            }
        }).doOnNext(new Consumer<Recipe>() {
            @Override
            public void accept(Recipe recipe) throws Exception {
                RecipeRepositoryImpl.this.recipe = recipe;
            }
        });
    }

    @Override
    public Observable<Recipe> getRecipe(long recipeId) {
        return Observable
                .concat(getRecipeFromMemory(recipeId), getRecipeFromDisk(recipeId))
                .subscribeOn(Schedulers.io())
                .take(1);
    }

    ContentValues getRecipeCV(Recipe recipe) {
        ContentValues cv = new ContentValues();
        cv.put(RecipeColumns.ID, recipe.getId());
        cv.put(RecipeColumns.NAME, recipe.getName());
        cv.put(RecipeColumns.IMAGE, recipe.getImage());
        cv.put(RecipeColumns.SERVINGS, recipe.getServings());
        cv.put(RecipeColumns.INGREDIENTS, gson.toJson(recipe.getIngredients()));
        cv.put(RecipeColumns.STEPS, gson.toJson(recipe.getSteps()));
        return cv;
    }

    Recipe getRecipeFromCursor(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(cursor.getColumnIndex(RecipeColumns.ID)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(RecipeColumns.NAME)));
        recipe.setImage(cursor.getString(cursor.getColumnIndex(RecipeColumns.IMAGE)));
        recipe.setServings(cursor.getInt(cursor.getColumnIndex(RecipeColumns.SERVINGS)));

        String ingredientsString = cursor.getString(cursor.getColumnIndex(RecipeColumns.INGREDIENTS));
        List<IngredientsItem> ingredientsItems = gson.fromJson(ingredientsString, ingredientsType);
        recipe.setIngredients(ingredientsItems);

        String stepsString = cursor.getString(cursor.getColumnIndex(RecipeColumns.STEPS));
        List<StepsItem> stepsItems = gson.fromJson(stepsString, stepsType);
        recipe.setSteps(stepsItems);

        return recipe;
    }
}
