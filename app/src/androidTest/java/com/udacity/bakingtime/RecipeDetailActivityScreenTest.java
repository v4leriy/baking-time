package com.udacity.bakingtime;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingtime.model.Ingredient;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.Step;
import com.udacity.bakingtime.ui.RecipeDetailActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityScreenTest {

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
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
            return intent;
        }
    };

    @Test
    public void checkViews_RecipeDetailActivity() {
        Ingredient ingredient = recipe.ingredients.get(0);

        Step step1 = recipe.steps.get(0);
        Step step2 = recipe.steps.get(1);

        onView(withId(R.id.recipe_ingredients))
                .check(matches(withText(startsWith("Ingredients"))))
                .check(matches(withText(containsString(ingredient.ingredient))))
                .check(matches(withText(containsString(String.valueOf(ingredient.quantity)))))
                .check(matches(withText(containsString(ingredient.measure))));

        onView(withText(step1.shortDescription)).check(matches(isDisplayed()));
        onView(withText(step2.shortDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerViewItem_OpensStepDetailActivity() {
        Step step = recipe.steps.get(1);

        onView(withId(R.id.recipe_steps_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recipe_step_description)).check(matches(withText(is(step.description))));
    }

}