package net.crescenthikari.bakingapp.features.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.RemoteViews;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.IngredientsItem;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetConfigureActivity RecipeWidgetConfigureActivity}
 */
public class RecipeWidget extends AppWidgetProvider {
    @Inject
    RecipeRepository recipeRepository;

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                String recipeName,
                                List<IngredientsItem> ingredients,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipe_widget_name, recipeName);
        StringBuilder sb = new StringBuilder();
        for (IngredientsItem item : ingredients) {
            sb.append("<p><b>").append(String.valueOf(item.getQuantity()))
                    .append(" ").append(item.getMeasure()).append("</b> ")
                    .append(item.getIngredient()).append("</p>");
        }
        views.setTextViewText(R.id.recipe_widget_ingredients, getSpannedText(sb.toString()));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            final int currentAppWidgetId = appWidgetId;
            int recipeId = RecipeWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
            recipeRepository
                    .getRecipe(recipeId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Recipe>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable disposable) {
                            // do nothing
                        }

                        @Override
                        public void onNext(@NonNull Recipe recipe) {
                            updateAppWidget(
                                    context,
                                    appWidgetManager,
                                    recipe.getName(),
                                    recipe.getIngredients(),
                                    currentAppWidgetId
                            );
                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {
                            // do nothing
                        }

                        @Override
                        public void onComplete() {
                            // do nothing
                        }
                    });
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

