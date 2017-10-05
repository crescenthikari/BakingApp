package net.crescenthikari.bakingapp.features.step.model.step;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public interface RecipeStepListData {
    int HEADER = 0;
    int INGREDIENT = 1;
    int STEP = 2;

    int getStepType();

    String getStepText();
}
