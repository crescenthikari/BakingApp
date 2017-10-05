package net.crescenthikari.bakingapp.di.module;

import android.content.ContentResolver;

import net.crescenthikari.bakingapp.data.api.RecipeService;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.data.repository.RecipeRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */

@Module
public class RecipeRepositoryModule {
    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(RecipeService recipeService,
                                             ContentResolver contentResolver) {
        return new RecipeRepositoryImpl(recipeService, contentResolver);
    }
}
