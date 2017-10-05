package net.crescenthikari.bakingapp.di.module;

import net.crescenthikari.bakingapp.features.list.RecipeListActivity;
import net.crescenthikari.bakingapp.features.step.RecipeStepDetailActivity;
import net.crescenthikari.bakingapp.features.step.RecipeStepsActivity;
import net.crescenthikari.bakingapp.features.widget.RecipeWidgetConfigureActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract RecipeListActivity contributeRecipeListActivityInjector();

    @ContributesAndroidInjector
    abstract RecipeStepsActivity contributeRecipeStepsActivityInjector();

    @ContributesAndroidInjector
    abstract RecipeStepDetailActivity contributeRecipeStepDetailActivityInjector();

    @ContributesAndroidInjector
    abstract RecipeWidgetConfigureActivity contributeRecipeWidgetConfigureActivityInjector();
}
