package com.artem.nsu.redditfeed.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditClient {

    private static volatile Retrofit sInstance = null;
    private static final String BASE_URL = "https://www.reddit.com/r/";

    public static Retrofit getInstance() {
        if (sInstance == null) {
            synchronized (RedditClient.class) {
                if (sInstance == null) {
                    sInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return sInstance;
    }

}
