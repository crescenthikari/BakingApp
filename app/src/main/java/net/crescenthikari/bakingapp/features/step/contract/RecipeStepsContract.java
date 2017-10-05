package net.crescenthikari.bakingapp.features.step.contract;

/**
 * Created by Muhammad Fiqri Muthohar on 9/19/17.
 */

public interface RecipeStepsContract {
    interface View {

    }

    interface Presenter {
        void bindRecipeSteps();

        void unbind();
    }
}
