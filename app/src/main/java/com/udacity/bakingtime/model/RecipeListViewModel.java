package com.udacity.bakingtime.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.bakingtime.R;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeListViewModel extends AndroidViewModel {

    private final MutableLiveData<RecipeList> remoteData;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        this.remoteData = new MutableLiveData<>();
        refreshRecipeList();
    }

    public LiveData<RecipeList> getRecipeListData() {
        return remoteData;
    }

    public void refreshRecipeList() {
        RecipeListTask task = new RecipeListTask(remoteData);
        task.execute();
    }

    class RecipeListTask extends AsyncTask<Void, Void, RecipeList> {

        private static final String LIST_URL = "http://go.udacity.com/android-baking-app-json";
        private static final String TAG = "RecipeListTask";

        private MutableLiveData<RecipeList> liveData;

        RecipeListTask(MutableLiveData<RecipeList> liveData) {
            this.liveData = liveData;
        }

        @Override
        protected RecipeList doInBackground(Void... voids) {
            String url = LIST_URL;
            RecipeList movieList = new RecipeList();
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Log.d(TAG, "Call: " + url);
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Log.d(TAG, "Got: " + json);

                if (response.isSuccessful()) {
                    List<Recipe> recipes = new Gson().fromJson(json, new TypeToken<List<Recipe>>(){}.getType());
                    if (recipes.size() > 0) {
                        movieList.setRecipes(recipes);
                    } else {
                        movieList.setMessage(getApplication().getString(R.string.msg_no_recipes));
                    }
                } else {
                    Log.e(TAG, "List recipes failed: " + response.message());
                    movieList.setMessage(getApplication().getString(R.string.msg_error, response.message()));
                }
            } catch (IOException e) {
                Log.e(TAG, "List recipes failed", e);
                movieList.setMessage(getApplication().getString(R.string.msg_no_connection));
            } catch (Exception e) {
                Log.e(TAG, "List recipes failed", e);
                movieList.setMessage(getApplication().getString(R.string.msg_error, e.getMessage()));
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(RecipeList movieList) {
            liveData.setValue(movieList);
        }
    }
}
