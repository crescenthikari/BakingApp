package net.crescenthikari.bakingapp.di.component.list;

import net.crescenthikari.bakingapp.features.list.RecipeListActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */

@Subcomponent
public interface RecipeListActivitySubcomponent extends AndroidInjector<RecipeListActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeListActivity> {
    }
}
