package net.crescenthikari.bakingapp.di.component.widget;

import net.crescenthikari.bakingapp.features.widget.RecipeWidgetConfigureActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 9/29/17.
 */

@Subcomponent
public interface RecipeWidgetConfigureActivitySubcomponent extends AndroidInjector<RecipeWidgetConfigureActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeWidgetConfigureActivity> {
    }
}
