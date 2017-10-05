package net.crescenthikari.bakingapp.features.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.features.widget.data.model.RecipeWidgetOption;
import net.crescenthikari.bakingapp.features.widget.data.transformer.RecipeToRecipeWidgetOptionBuilder;
import net.crescenthikari.bakingapp.features.widget.data.transformer.RecipeWidgetOptionBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "net.crescenthikari.bakingapp.features.widget.RecipeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.appwidget_recipe_radio_group)
    RadioGroup recipeRadioGroup;

    @BindView(R.id.add_button)
    Button addWidgetButton;

    @Inject
    RecipeRepository recipeRepository;

    RecipeWidgetOptionBuilder optionBuilder;

    CompositeDisposable compositeDisposable;

    public RecipeWidgetConfigureActivity() {
        super();
        optionBuilder = new RecipeToRecipeWidgetOptionBuilder();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        AndroidInjection.inject(this);
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);
        ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setupRecipeOptions();

//        mAppWidgetText.setText(loadRecipePref(RecipeWidgetConfigureActivity.this, mAppWidgetId));
    }

    private void setupRecipeOptions() {
        recipeRepository
                .getRecipes()
                .flatMap(new Function<List<Recipe>, ObservableSource<List<RecipeWidgetOption>>>() {
                    @Override
                    public ObservableSource<List<RecipeWidgetOption>> apply(@NonNull List<Recipe> recipes) throws Exception {
                        List<RecipeWidgetOption> options = new ArrayList<>();
                        for (Recipe recipe : recipes) {
                            options.add(optionBuilder.fromData(recipe).build());
                        }
                        return Observable.just(options);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RecipeWidgetOption>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(@NonNull List<RecipeWidgetOption> recipeWidgetOptions) {
                        for (RecipeWidgetOption option : recipeWidgetOptions) {
                            RadioButton radio = new RadioButton(RecipeWidgetConfigureActivity.this);
                            radio.setId(option.getRecipeId());
                            radio.setText(option.getRecipeName());
                            recipeRadioGroup.addView(radio);
                        }
                        recipeRadioGroup.check(loadRecipePref(
                                RecipeWidgetConfigureActivity.this,
                                mAppWidgetId
                        ));
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Toast.makeText(
                                RecipeWidgetConfigureActivity.this,
                                "Whoops! Something wrong happened!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @OnClick(R.id.add_button)
    public void addWidget() {
        final Context context = RecipeWidgetConfigureActivity.this;

        int recipeId = recipeRadioGroup.getCheckedRadioButtonId();

        if (recipeId != RadioGroup.NO_ID) {
            // When the button is clicked, store the string locally
            saveRecipePref(context, mAppWidgetId, recipeId);

            recipeRepository.getRecipe(recipeId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Recipe>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable disposable) {

                        }

                        @Override
                        public void onNext(@NonNull Recipe recipe) {
                            // It is the responsibility of the configuration activity to update the app widget
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                            RecipeWidget.updateAppWidget(
                                    context,
                                    appWidgetManager,
                                    recipe.getName(),
                                    recipe.getIngredients(),
                                    mAppWidgetId
                            );

                            // Make sure we pass back the original appWidgetId
                            Intent resultValue = new Intent();
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                            setResult(RESULT_OK, resultValue);
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toast.makeText(
                    context,
                    R.string.no_recipe_selected,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

}

