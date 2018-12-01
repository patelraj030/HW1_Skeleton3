package com.example.rkjc.news_app_2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.rkjc.news_app_2.model.NewsItemResponse;

import java.util.List;

@Dao
public interface NewsItemDao {

    @Query("SELECT * FROM news_item")
    LiveData<List<NewsItemResponse.NewsItem>> loadAllNewsItem();

    @Insert
    void insert(List<NewsItemResponse.NewsItem> newsItemList);

    @Query("DELETE FROM news_item")
    void clearAll();

}

