package com.example.rkjc.news_app_2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.rkjc.news_app_2.data.NewsItemRepository;
import com.example.rkjc.news_app_2.database.AppDatabase;
import com.example.rkjc.news_app_2.model.NewsItemResponse;

import java.util.List;

public class NewsItemViewModel extends ViewModel {

    AppDatabase mAppDatabase;
    private LiveData<List<NewsItemResponse.NewsItem>> newsList;

    public LiveData<List<NewsItemResponse.NewsItem>> getNewsList(Context context) {
        mAppDatabase = AppDatabase.getsInstance(context.getApplicationContext());

        if (newsList == null) {
            NewsItemRepository newsItemRepository = new NewsItemRepository(mAppDatabase);
            newsList = newsItemRepository.getAllNewsFromDatabase();
        }
        return newsList;
    }
}
