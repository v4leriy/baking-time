package com.udacity.bakingtime.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Ingredient;
import com.udacity.bakingtime.model.Recipe;

/**
 *
 */
public class RecipeStepsFragment extends Fragment {

    public static final String SELECTION = "selection";

    private OnStepClickListener mCallback;
    private RecyclerView recyclerView;
    private TextView ingredientsView;
    private RecipeStepsAdapter adapter;
    private Recipe recipe;
    private boolean twoPane;
    private int selection;

    // OnStepClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(int position);
    }


    public RecipeStepsFragment() {
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        ingredientsView = rootView.findViewById(R.id.recipe_ingredients);

        recyclerView = rootView.findViewById(R.id.recipe_steps_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (savedInstanceState != null) {
            selection = savedInstanceState.getInt(SELECTION, 0);
        }

        // Return the root view
        return rootView;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        if (recipe != null) {
            adapter = new RecipeStepsAdapter(getContext(), recipe, position -> {
                mCallback.onStepSelected(position);
            });
            adapter.setSelection(twoPane ? selection : -1);
            recyclerView.setAdapter(adapter);

            StringBuilder sb = new StringBuilder(getString(R.string.ingredients_label));
            for (Ingredient ingredient : recipe.ingredients) {
                sb.append('\n');
                sb.append(ingredient.ingredient);
                sb.append(" - ");
                sb.append(ingredient.quantity);
                sb.append(" ");
                sb.append(ingredient.measure);
            }
            ingredientsView.setText(sb.toString());
        } else {
            ingredientsView.setText("");
        }
    }

    public boolean isTwoPane() {
        return twoPane;
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
        if (adapter != null) {
            adapter.setSelection(twoPane ? selection : -1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        if (adapter != null) {
            currentState.putInt(SELECTION, adapter.getSelection());
        }
    }
}
