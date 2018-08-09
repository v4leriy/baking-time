package com.udacity.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Ingredient implements Parcelable {

    public double quantity;
    public String measure;
    public String ingredient;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    private void readFromParcel(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel in) {
            Ingredient ingredient = new Ingredient();
            ingredient.readFromParcel(in);
            return ingredient;
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}
