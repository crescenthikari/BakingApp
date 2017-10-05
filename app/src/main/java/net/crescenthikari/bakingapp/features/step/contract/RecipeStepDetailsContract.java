package net.crescenthikari.bakingapp.features.step.contract;

/**
 * Created by Muhammad Fiqri Muthohar on 9/19/17.
 */

public interface RecipeStepDetailsContract {
    interface View {

    }

    interface Presenter {
        void bindRecipeStepDetails();

        void unbind();
    }
}
