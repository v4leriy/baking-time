package com.udacity.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Ingredient;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.ui.RecipeWidgetConfigure;

/**
 *
 */
public class RecipeListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int appWidgetId;
    private Recipe recipe;

    public RecipeListProvider(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.recipe = RecipeWidgetConfigure.getRecipe(context, appWidgetId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.ingredients.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = recipe.ingredients.get(position);
        String text = ingredient.ingredient + " - " + ingredient.quantity + " " + ingredient.measure;
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_item);
        remoteView.setTextViewText(R.id.recipe_ingredient, text);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}