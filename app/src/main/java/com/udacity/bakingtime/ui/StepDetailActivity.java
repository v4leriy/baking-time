package com.udacity.bakingtime.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.Step;

/**
 *
 */
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(RecipeDetailActivity.EXTRA_RECIPE);
        setTitle(recipe.name);

        int recipeStepIndex = intent.getIntExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP_INDEX, 0);
        Step step = recipe.steps.get(recipeStepIndex);
        boolean fullScreen = !TextUtils.isEmpty(step.videoURL) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStep(step);
            stepDetailFragment.setFullScreen(fullScreen);

            // Add the fragment to its container using a FragmentManager and a Transaction
            fragmentManager.beginTransaction().add(R.id.step_detail_container, stepDetailFragment).commit();
        } else {
            StepDetailFragment stepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentById(R.id.step_detail_container);
            stepDetailFragment.setFullScreen(fullScreen);
        }

        if (fullScreen) {
            getSupportActionBar().hide();
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
