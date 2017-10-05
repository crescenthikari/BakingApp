package net.crescenthikari.bakingapp.features.list;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.crescenthikari.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityUITest {
    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void verifyRecipeData() {
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));

        onView(withText("Brownies")).check(matches(isDisplayed()));

        onView(withText("Yellow Cake")).check(matches(isDisplayed()));

        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

}