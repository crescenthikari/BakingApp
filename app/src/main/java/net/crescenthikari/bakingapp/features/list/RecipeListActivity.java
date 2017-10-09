package net.crescenthikari.bakingapp.features.list;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.features.list.adapter.RecipeListAdapter;
import net.crescenthikari.bakingapp.features.list.contract.OnRecipeItemClickListener;
import net.crescenthikari.bakingapp.features.list.contract.RecipeListContract;
import net.crescenthikari.bakingapp.features.list.presenter.RecipeListPresenter;
import net.crescenthikari.bakingapp.features.step.RecipeStepsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class RecipeListActivity extends AppCompatActivity
        implements RecipeListContract.View, OnRecipeItemClickListener {
    private static final String TAG = "RecipeListActivity";

    IdlingResource idlingResource;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipe_list)
    RecyclerView recipeListView;

    @BindView(R.id.recipe_list_message_field)
    TextView recipeListMessageField;

    @BindView(R.id.recipe_list_progressbar)
    ProgressBar recipeListProgressBar;

    @Inject
    RecipeRepository recipeRepository;

    RecipeListAdapter recipeListAdapter;

    RecipeListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setupViews();

        presenter = new RecipeListPresenter(this, recipeRepository);

    }

    private void setupViews() {
        setupToolbar();
        setupRecipeListView();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecipeListView() {
        recipeListAdapter = new RecipeListAdapter();
        recipeListAdapter.setItemClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_count));
        recipeListView.setLayoutManager(gridLayoutManager);
        recipeListView.setItemAnimator(new DefaultItemAnimator());
        recipeListView.setAdapter(recipeListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.bindRecipeList();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.unbind();
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.menu_refresh_recipe) {
            if (presenter != null) {
                presenter.getRecipeList();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRecipeList(List<Recipe> recipes) {
        recipeListAdapter.setRecipes(recipes);
        recipeListAdapter.notifyDataSetChanged();
        recipeListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecipeList() {
        recipeListView.setVisibility(View.GONE);
    }

    @Override
    public void hideRecipeListMessage() {
        recipeListMessageField.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        recipeListProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        recipeListProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyRecipeListMessage() {
        recipeListMessageField.setVisibility(View.VISIBLE);
        recipeListMessageField.setText(R.string.no_recipes_found_error_message);
    }

    @Override
    public void onRecipeItemClicked(Recipe recipe) {
        Log.d(TAG, "onRecipeItemClicked: " + recipe.getId());
        RecipeStepsActivity.showRecipeSteps(this, recipe.getId(), recipe.getName());
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new IdlingResource() {
                @Override
                public String getName() {
                    return "RecipeListActivity";
                }

                @Override
                public boolean isIdleNow() {
                    return false;
                }

                @Override
                public void registerIdleTransitionCallback(ResourceCallback callback) {

                }
            };
        }
        return idlingResource;
    }
}
