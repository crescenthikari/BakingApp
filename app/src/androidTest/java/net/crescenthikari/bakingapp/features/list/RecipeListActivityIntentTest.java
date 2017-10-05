package net.crescenthikari.bakingapp.features.list;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.crescenthikari.bakingapp.features.step.RecipeStepsActivity.KEY_RECIPE_ID;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityIntentTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> intentsTestRule
            = new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void testRecipeStepsActivityIntent() {
        onView(withText("Brownies")).perform(click());
        intended(hasExtraWithKey(KEY_RECIPE_ID));
        intended(hasExtra(KEY_RECIPE_ID, 2L));
    }
}