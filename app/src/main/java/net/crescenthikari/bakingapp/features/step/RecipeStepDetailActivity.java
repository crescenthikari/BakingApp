package net.crescenthikari.bakingapp.features.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.crescenthikari.bakingapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity
        implements HasSupportFragmentInjector {
    public static final String KEY_RECIPE_ID = "RECIPE_ID";
    public static final String KEY_STEP_ID = "STEP_ID";

    public static void showStepDetailPage(Context context, long recipeId, long stepId) {
        Intent detailIntent = new Intent(context, RecipeStepDetailActivity.class);
        detailIntent.putExtra(KEY_RECIPE_ID, recipeId);
        detailIntent.putExtra(KEY_STEP_ID, stepId);
        context.startActivity(detailIntent);
    }

    private long recipeId;
    private long stepId;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        recipeId = getIntent().getLongExtra(KEY_RECIPE_ID, 0);
        stepId = getIntent().getLongExtra(KEY_STEP_ID, 0);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(recipeId, stepId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            navigateUpTo(new Intent(this, RecipeStepsActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidFragmentInjector;
    }
}
