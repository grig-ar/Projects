package com.artem.nsu.redditfeed.db.entity;

import com.artem.nsu.redditfeed.model.ISubreddit;

public class Subreddit implements ISubreddit {

    private String id;

    private String name;

    private String url;

    private String thumbnailUrl;

    public Subreddit(String id, String url, String thumbnailUrl) {
        this.id = id;
        int begin = url.indexOf("/", 1);
        int end = url.indexOf("/", 3);
        this.name = url.substring(begin + 1, end);
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
