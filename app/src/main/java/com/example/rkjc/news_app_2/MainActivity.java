package com.example.rkjc.news_app_2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rkjc.news_app_2.data.NewsItemRepository;
import com.example.rkjc.news_app_2.database.AppDatabase;
import com.example.rkjc.news_app_2.model.NewsItemResponse;
import com.example.rkjc.news_app_2.sync.NewsSyncFirebaseJobService;
import com.example.rkjc.news_app_2.sync.NewsSyncUtilities;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    RecyclerView mNewsRecyclerView;
    TextView mErrorText;

    private final String TAG = MainActivity.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private NewsItemRepository mNewsItemRepository;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mNewsRecyclerView = findViewById(R.id.news_recyclerview);
        mErrorText = findViewById(R.id.error_text);

        mAppDatabase = AppDatabase.getsInstance(getApplicationContext());

        mNewsItemRepository = new NewsItemRepository(mAppDatabase);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(MainActivity.this,
                new ArrayList<NewsItemResponse.NewsItem>());
        mNewsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        setupViewModel();

        NewsSyncUtilities.scheduleJob(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.get_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            if (isNetworkConnected()) {
                mNewsItemRepository.getNewsFromAPIAndStoreIntoDatabase();
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupViewModel() {
        NewsItemViewModel newsItemViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);
        newsItemViewModel.getNewsList(MainActivity.this).observe(this, new Observer<List<NewsItemResponse.NewsItem>>() {
            @Override
            public void onChanged(@Nullable List<NewsItemResponse.NewsItem> newsItems) {
                Log.d(TAG, "onChanged");
                mProgressBar.setVisibility(View.GONE);
                if (newsItems != null) {
                    Log.d(TAG, newsItems.toString());
                    newsRecyclerViewAdapter.setNewsItemArrayList(new ArrayList<>(newsItems));
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
