package net.crescenthikari.bakingapp.di.module;

import net.crescenthikari.bakingapp.features.widget.RecipeWidget;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Muhammad Fiqri Muthohar on 9/29/17.
 */

@Module
public abstract class BroadcastReceiverBuilder {
    @ContributesAndroidInjector
    abstract RecipeWidget contributeRecipeWidgetInjector();
}
