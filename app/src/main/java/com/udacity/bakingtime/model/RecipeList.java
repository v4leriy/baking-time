package com.udacity.bakingtime.model;

import java.util.List;

public class RecipeList {

    private List<Recipe> recipes;
    private String message;

    public RecipeList() {
    }

    public RecipeList(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return recipes == null && message != null;
    }
}
