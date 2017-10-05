package net.crescenthikari.bakingapp.di.component.step;

import net.crescenthikari.bakingapp.features.step.RecipeStepsActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 9/19/17.
 */

@Subcomponent
public interface RecipeStepsActivitySubcomponent extends AndroidInjector<RecipeStepsActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeStepsActivity> {
    }
}
