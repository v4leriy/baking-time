package com.udacity.bakingtime.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;

/**
 *
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {

    public RecipeAdapter(Activity context, int resource) {
        super(context, resource);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Recipe recipe = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.recipe_image);
        if (!TextUtils.isEmpty(recipe.image)) {
            Picasso.with(getContext()).load(recipe.image).into(imageView);
        } else {
            // placeholder?
            imageView.setImageResource(0);
        }

        TextView textView = convertView.findViewById(R.id.recipe_name);
        textView.setText(recipe.name);

        return convertView;
    }
}
