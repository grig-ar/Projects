package com.artem.nsu.redditfeed;

import android.app.Application;

import com.artem.nsu.redditfeed.api.RedditClient;
import com.artem.nsu.redditfeed.api.IRedditService;
import com.artem.nsu.redditfeed.db.RedditDatabase;
import com.artem.nsu.redditfeed.repository.DataRepository;

public class RedditApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
    }

    public RedditDatabase getDatabase() {
        return RedditDatabase.getInstance(this, mAppExecutors);
    }

    public IRedditService getApi() {
        return RedditClient.getInstance().create(IRedditService.class);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase(), getApi());
    }

}
