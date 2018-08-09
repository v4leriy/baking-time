package com.udacity.bakingtime;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingtime.model.Ingredient;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.Step;
import com.udacity.bakingtime.ui.RecipeDetailActivity;
import com.udacity.bakingtime.ui.StepDetailActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityScreenTest {

    private static Recipe recipe;

    @BeforeClass
    public static void createRecipe() {
        Ingredient ingredient = new Ingredient();
        ingredient.ingredient = "test ingredient";
        ingredient.quantity = 2.5d;
        ingredient.measure = "test measure";

        Step step1 = new Step();
        step1.shortDescription = "test short description1";
        step1.description = "test short description1";

        Step step2 = new Step();
        step2.shortDescription = "test short description2";
        step2.description = "test short description2";

        recipe = new Recipe();
        recipe.name = "test recipe";
        recipe.ingredients = Arrays.asList(ingredient);
        recipe.steps = Arrays.asList(step1, step2);
    }

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityTestRule = new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_INDEX, 1);
            return intent;
        }
    };

    @Test
    public void checkViews_StepDetailActivity() {
        Step step = recipe.steps.get(1);

        onView(withId(R.id.recipe_step_description)).check(matches(withText(is(step.description))));
    }

}