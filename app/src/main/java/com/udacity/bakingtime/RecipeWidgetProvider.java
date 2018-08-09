package com.udacity.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.bakingtime.model.Ingredient;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.ui.MainActivity;
import com.udacity.bakingtime.ui.RecipeDetailActivity;
import com.udacity.bakingtime.ui.RecipeWidgetConfigure;

public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Recipe recipe = RecipeWidgetConfigure.getRecipe(context, appWidgetId);
            RemoteViews rv = getRecipeRemoteView(context, recipe, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    public static RemoteViews getRecipeRemoteView(Context context, Recipe recipe, int appWidgetId) {
        // Set the click handler to open the DetailActivity for recipe,
        // or the MainActivity if recipe is invalid
        Intent intent;
        if (recipe == null) {
            intent = new Intent(context, MainActivity.class);
        } else { // Set on click to open the corresponding detail activity
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        }
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        // text
        if (recipe == null) {
            views.setTextViewText(R.id.recipe_ingredients, context.getString(R.string.select_recipe_text));
        } else {
            StringBuilder sb = new StringBuilder(recipe.name);
            sb.append('\n');
            sb.append(context.getString(R.string.ingredients_label));
            for (Ingredient ingredient : recipe.ingredients) {
                sb.append('\n');
                sb.append(ingredient.ingredient);
                sb.append(" - ");
                sb.append(ingredient.quantity);
                sb.append(" ");
                sb.append(ingredient.measure);
            }
            views.setTextViewText(R.id.recipe_ingredients, sb.toString());
        }
        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.recipe_ingredients, pendingIntent);
        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
