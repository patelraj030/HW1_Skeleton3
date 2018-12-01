package com.example.rkjc.news_app_2.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.rkjc.news_app_2.JsonUtils;
import com.example.rkjc.news_app_2.NetworkUtils;
import com.example.rkjc.news_app_2.database.AppDatabase;
import com.example.rkjc.news_app_2.model.NewsItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsItemRepository {

    private static final String TAG = NewsItemRepository.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private LiveData<List<NewsItemResponse.NewsItem>> data;

    public NewsItemRepository(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    public LiveData<List<NewsItemResponse.NewsItem>> getAllNewsFromDatabase() {
        try {
            return new GetNewsFromDatabase().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getNewsFromAPIAndStoreIntoDatabase() {
        new NewsQueryTask().execute();
    }

    class GetNewsFromDatabase extends AsyncTask<Void, Void, LiveData<List<NewsItemResponse.NewsItem>>> {

        @Override
        protected LiveData<List<NewsItemResponse.NewsItem>> doInBackground(Void... voids) {
            return mAppDatabase.newsItemDao().loadAllNewsItem();
        }

        @Override
        protected void onPostExecute(LiveData<List<NewsItemResponse.NewsItem>> listLiveData) {
            data = listLiveData;
        }
    }

    private class NewsQueryTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                mAppDatabase.newsItemDao().clearAll();
                return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString != null) {

                ArrayList<NewsItemResponse.NewsItem> loNewsItemsNewsItemArrayList = JsonUtils.parseNews(jsonString);
                if (loNewsItemsNewsItemArrayList != null) {
                    new StoreNewsInDatabase(loNewsItemsNewsItemArrayList).execute();
                } else {

                }

            } else {
            }
        }
    }

    class StoreNewsInDatabase extends AsyncTask<Void, Void, Void> {
        ArrayList<NewsItemResponse.NewsItem> mNewsItemArrayList;

        StoreNewsInDatabase(ArrayList<NewsItemResponse.NewsItem> foNewsItemArrayList) {
            mNewsItemArrayList = foNewsItemArrayList;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mAppDatabase.newsItemDao().insert(mNewsItemArrayList);
            return null;
        }
    }

}
