package com.udacity.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.ui.RecipeDetailActivity;
import com.udacity.bakingtime.ui.RecipeWidgetConfigure;

public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Recipe recipe = RecipeWidgetConfigure.getRecipe(context, appWidgetId);
            if (recipe != null) {
                RemoteViews rv = getRecipeRemoteView(context, recipe, appWidgetId);
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        }
    }

    public static RemoteViews getRecipeRemoteView(Context context, Recipe recipe, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipe_name, recipe.name);
        views.setTextViewText(R.id.recipe_ingredients_label, context.getString(R.string.ingredients_label));

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, RecipeWidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.recipe_ingredients, svcIntent);

        // Set the click handler to open the DetailActivity for recipe
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.recipe_widget_layout, pendingIntent);

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
