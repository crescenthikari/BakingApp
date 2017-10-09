package net.crescenthikari.bakingapp.features.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ViewGroup;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.model.StepsItem;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.features.step.adapter.RecipeStepAdapter;
import net.crescenthikari.bakingapp.features.step.contract.OnStepClickListener;
import net.crescenthikari.bakingapp.features.step.model.step.HeaderData;
import net.crescenthikari.bakingapp.features.step.model.step.IngredientData;
import net.crescenthikari.bakingapp.features.step.model.step.RecipeStepListData;
import net.crescenthikari.bakingapp.features.step.model.step.StepData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepsActivity extends AppCompatActivity
        implements OnStepClickListener, HasSupportFragmentInjector {

    public static final String KEY_RECIPE_ID = "RECIPE_ID";
    public static final String KEY_RECIPE_NAME = "RECIPE_NAME";
    public static final String KEY_RV_STATE = "RV_STATE";
    public static final int UNDEFINED_ID = -1;
    @Inject
    RecipeRepository recipeRepository;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_step_list)
    RecyclerView recipeStepListView;

    @Nullable
    @BindView(R.id.recipe_step_detail_container)
    ViewGroup detailContainer;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

    private long recipeId;
    private String recipeName;

    private Parcelable rvState;
    private GridLayoutManager rvLayoutManager;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeStepAdapter recipeStepAdapter;

    private CompositeDisposable disposables;

    public static void showRecipeSteps(@NonNull Context context, long recipeId, String recipeName) {
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putExtra(KEY_RECIPE_ID, recipeId);
        intent.putExtra(KEY_RECIPE_NAME, recipeName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_list);
        ButterKnife.bind(this);

        disposables = new CompositeDisposable();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRecyclerView(recipeStepListView);

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong(KEY_RECIPE_ID, UNDEFINED_ID);
            recipeName = savedInstanceState.getString(KEY_RECIPE_NAME, "");
        } else if (getIntent() != null) {
            recipeId = getIntent().getLongExtra(KEY_RECIPE_ID, UNDEFINED_ID);
            recipeName = getIntent().getStringExtra(KEY_RECIPE_NAME);
        }

        if (!TextUtils.isEmpty(recipeName)) {
            getSupportActionBar().setTitle(recipeName);
            getSupportActionBar().setSubtitle(getTitle());
        } else {
            getSupportActionBar().setTitle(getTitle());
            getSupportActionBar().setSubtitle("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRecipeDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rvState != null) {
            rvLayoutManager.onRestoreInstanceState(rvState);
        }
    }

    private void getRecipeDetail() {
        recipeRepository.getRecipe(recipeId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Recipe, ObservableSource<List<RecipeStepListData>>>() {
                    @Override
                    public ObservableSource<List<RecipeStepListData>> apply(@io.reactivex.annotations.NonNull Recipe recipe) throws Exception {
                        List<RecipeStepListData> data = new ArrayList<>();
                        data.add(new HeaderData("Ingredients"));
                        data.add(new IngredientData(recipe.getIngredients()));
                        data.add(new HeaderData("Steps"));
                        for (StepsItem item : recipe.getSteps()) {
                            data.add(new StepData(item));
                        }
                        return Observable.just(data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RecipeStepListData>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<RecipeStepListData> recipeStepListDatas) {
                        recipeStepAdapter.setRecipeStepListDatas(recipeStepListDatas);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        // do nothing
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_RECIPE_ID, recipeId);
        outState.putString(KEY_RECIPE_NAME, recipeName);
        rvState = rvLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_RV_STATE, rvState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rvState = savedInstanceState.getParcelable(KEY_RV_STATE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rvLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(rvLayoutManager);
        recipeStepAdapter = new RecipeStepAdapter();
        recipeStepAdapter.setOnStepClickListener(this);
        recyclerView.setAdapter(recipeStepAdapter);
    }

    @Override
    public void onStepClick(StepData data) {
        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.recipe_step_detail_container,
                            RecipeStepDetailFragment.newInstance(recipeId, data.getStepId())
                    )
                    .commit();
        } else {
            RecipeStepDetailActivity.showStepDetailPage(this, recipeId, data.getStepId());
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidFragmentInjector;
    }
}
