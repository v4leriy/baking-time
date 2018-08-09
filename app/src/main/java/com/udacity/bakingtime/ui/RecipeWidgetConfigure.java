package com.udacity.bakingtime.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udacity.bakingtime.R;
import com.udacity.bakingtime.RecipeWidgetProvider;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.RecipeListViewModel;

/**
 *
 */
public class RecipeWidgetConfigure extends AppCompatActivity {

    private RecipeListViewModel recipeListViewModel;
    private RecipeAdapter adapter;
    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.select_recipe_text);

        Intent intent = getIntent();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setResult(RESULT_CANCELED);
        adapter = new RecipeAdapter(this, R.layout.recipe_item);

        GridView recipesView = findViewById(R.id.recipes_view);
        recipesView.setAdapter(adapter);
        recipesView.setOnItemClickListener((adapterView, view, position, l) -> configureWidget(adapter.getItem(position)));

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

    private void configureWidget(Recipe recipe) {
        RemoteViews remoteViews = RecipeWidgetProvider.getRecipeRemoteView(this, recipe, appWidgetId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        SharedPreferences preferences = getSharedPreferences("widget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("recipe" + appWidgetId, new Gson().toJson(recipe));
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public static Recipe getRecipe(Context context, int appWidgetId) {
        SharedPreferences preferences = context.getSharedPreferences("widget", Context.MODE_PRIVATE);
        String json = preferences.getString("recipe" + appWidgetId, null);
        if (json != null) {
            return new Gson().fromJson(json, Recipe.class);
        }
        return null;
    }
}
