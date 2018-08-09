package com.udacity.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;

/**
 *
 */
public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.OnStepClickListener {

    public static final String EXTRA_RECIPE = "recipe";
    public static final String EXTRA_RECIPE_STEP_INDEX = "recipeStepIndex";

    private Recipe recipe;

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        setTitle(recipe.name);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Determine if you're creating a two-pane or single-pane display
        if (findViewById(R.id.step_detail_container) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            twoPane = true;

            if (savedInstanceState == null) {
                // Creating a new head fragment
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStep(recipe.steps.get(0));
                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction().add(R.id.step_detail_container, stepDetailFragment).commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            twoPane = false;
        }

        RecipeStepsFragment listFragment = (RecipeStepsFragment) fragmentManager.findFragmentById(R.id.recipe_steps_fragment);
        listFragment.setTwoPane(twoPane);
        listFragment.setRecipe(recipe);

    }

    @Override
    public void onStepSelected(int position) {
        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (twoPane) {
            // Create two=pane interaction
            StepDetailFragment newFragment = new StepDetailFragment();
            newFragment.setStep(recipe.steps.get(position));

            getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, newFragment).commit();
        } else {
            // Put this information in a Bundle and attach it to an Intent that will launch an StepDetailActivity
            Bundle b = new Bundle();
            b.putParcelable(EXTRA_RECIPE, recipe);
            b.putInt(EXTRA_RECIPE_STEP_INDEX, position);

            // Attach the Bundle to an intent
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

}
