package net.crescenthikari.bakingapp.features.widget.data.transformer;

import net.crescenthikari.bakingapp.features.widget.data.model.RecipeWidgetOption;

/**
 * Created by Muhammad Fiqri Muthohar on 9/29/17.
 */

public interface RecipeWidgetOptionBuilder<T> {
    RecipeWidgetOptionBuilder fromData(T data);

    RecipeWidgetOption build();
}
