package net.crescenthikari.bakingapp.features.list.contract;

import net.crescenthikari.bakingapp.data.model.Recipe;

import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 9/18/17.
 */

public interface RecipeListContract {
    interface View {
        void showRecipeList(List<Recipe> recipes);

        void hideRecipeList();

        void showEmptyRecipeListMessage();

        void hideRecipeListMessage();

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter {
        void bindRecipeList();

        void getRecipeList();

        void unbind();
    }
}
