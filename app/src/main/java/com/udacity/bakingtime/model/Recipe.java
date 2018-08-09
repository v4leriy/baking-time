package com.udacity.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Recipe implements Parcelable {

    public int id;
    public String name;
    public int servings;
    public String image;
    public List<Ingredient> ingredients;
    public List<Step> steps;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        in.readTypedList(ingredients = new ArrayList<>(), Ingredient.CREATOR);
        in.readTypedList(steps = new ArrayList<>(), Step.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel in) {
            Recipe recipe = new Recipe();
            recipe.readFromParcel(in);
            return recipe;
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
