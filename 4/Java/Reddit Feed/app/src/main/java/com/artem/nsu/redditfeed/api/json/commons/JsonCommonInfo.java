package com.artem.nsu.redditfeed.api.json.commons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonCommonInfo implements Serializable {

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("subreddit")
    @Expose
    private String sub;

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("permalink")
    @Expose
    private String permaLink;

    @SerializedName("created")
    @Expose
    private String created;

    public JsonCommonInfo(String id, String author, String sub, String score,
                          String permaLink, String created) {
        this.author = author;
        this.id = id;
        this.sub = sub;
        this.score = score;
        this.permaLink = permaLink;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
