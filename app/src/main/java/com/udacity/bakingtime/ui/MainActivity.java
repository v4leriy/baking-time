package com.udacity.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.RecipeListViewModel;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private RecipeListViewModel recipeListViewModel;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new RecipeAdapter(this, R.layout.recipe_item);

        GridView recipesView = findViewById(R.id.recipes_view);
        recipesView.setAdapter(adapter);
        recipesView.setOnItemClickListener((adapterView, view, position, l) -> launchDetailActivity(adapter.getItem(position)));

        final TextView msgView = findViewById(R.id.message_view);
        recipesView.setEmptyView(msgView);

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        recipeListViewModel.getRecipeListData().observe(this, recipeList -> {
            adapter.clear();
            if (recipeList != null) {
                if (recipeList.isError()) {
                    msgView.setText(recipeList.getMessage());
                } else {
                    adapter.addAll(recipeList.getRecipes());
                }
            } else {
                msgView.setText(R.string.msg_no_recipes);
            }
        });
    }

    private void launchDetailActivity(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
