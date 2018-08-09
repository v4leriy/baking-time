package com.udacity.bakingtime.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Recipe;
import com.udacity.bakingtime.model.Step;

/**
 *
 */
public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.StepViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private Context context;
    private Recipe recipe;
    private OnItemClickListener listener;

    private int selection = 0;

    public RecipeStepsAdapter(Context context, Recipe recipe, OnItemClickListener listener) {
        this.context = context;
        this.recipe = recipe;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = recipe.steps.get(position);
        holder.descriptionView.setText(step.shortDescription);
        if (!TextUtils.isEmpty(step.thumbnailURL)) {
            Picasso.with(context).load(step.thumbnailURL).into(holder.imageView);
        } else {
            // placeholder?
            holder.imageView.setImageResource(0);
        }
        if (selection >= 0) {
            if (selection == position) {
                holder.descriptionView.setBackgroundColor(Color.CYAN);
            } else {
                holder.descriptionView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        holder.itemView.setOnClickListener(l -> {
            if (selection >= 0 && selection != position) {
                int old = selection;
                selection = position;
                notifyItemChanged(old);
                notifyItemChanged(selection);
            }
            listener.onItemClick(position);
        });
    }

    @Override
    public long getItemId(int i) {
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        return recipe == null ? 0 : recipe.steps.size();
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView descriptionView;

        StepViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_step_image);
            descriptionView = itemView.findViewById(R.id.recipe_step_description);
        }

    }

}
