package com.artem.nsu.redditfeed.db.entity;

import com.artem.nsu.redditfeed.api.json.post.JsonPostEntry;
import com.artem.nsu.redditfeed.api.json.post.JsonPostInfo;
import com.artem.nsu.redditfeed.model.IPost;
import com.artem.nsu.redditfeed.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table",
        indices = {@Index(value = "subreddit")}
        )
public class PostEntity implements IPost {

    @PrimaryKey
    @NonNull
    private String id;

    private String title;

    private String author;

    private String text;

    private String created;

    private String subreddit;

    private String image;

    private String url;

    private String videoUrl;

    private String comments;

    private String postHint;

    private String score;

    private Boolean isFavorite;

    private String numComments;

    private Long addedTime = System.currentTimeMillis();

    private Boolean isPopular;

    public PostEntity(@NonNull String id, String title, String author, String text, String created, String subreddit,
                      String image, String url, String comments, String score, String numComments,
                      String postHint, Boolean isFavorite, Boolean isPopular, String videoUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.text = text;
        this.created = created;
        this.subreddit = subreddit;
        this.image = image;
        this.url = url;
        this.comments = comments;
        this.postHint = postHint;
        this.videoUrl = videoUrl;
        this.score = score;
        this.numComments = numComments;
        this.isFavorite = isFavorite;
        this.isPopular = isPopular;
    }

    @Ignore
    public static List<PostEntity> createPostsListFromEntries(List<JsonPostEntry> entries, Boolean isPopular) {
        List<PostEntity> postsList = new ArrayList<>(entries.size());
        for (JsonPostEntry entry : entries) {
            JsonPostInfo info = entry.getPostInfo();
            long epochTime = (long) Math.round(Float.parseFloat(info.getCreated())) * 1000L;
            String videoUrl = null;
            if (info.getPostHint() != null && info.getPostHint().equals("hosted:video")) {
                videoUrl = info.getMedia().getPostVideo().getVideoUrl();
            }
            postsList.add(new PostEntity(entry.getKind() + '_' + info.getId(), info.getTitle(),
                    info.getAuthor(), info.getText(), DateFormatter.formatEpochTime(epochTime), info.getSub(),
                    info.getImage(), info.getUrl(), "https://www.reddit.com" + info.getPermaLink(),
                    info.getScore(), info.getNumComments(), info.getPostHint(), false,
                    isPopular, videoUrl));
        }
        return postsList;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setAddedTime(Long addedTime) {
        this.addedTime = addedTime;
    }

    public void setPopular(Boolean popular) {
        isPopular = popular;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getComments() {
        return comments;
    }

    public String getPostHint() {
        return postHint;
    }

    public String getScore() {
        return score;
    }

    public String getNumComments() {
        return numComments;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Long getAddedTime() {
        return addedTime;
    }

    public Boolean isPopular() {
        return isPopular;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPostHint(String postHint) {
        this.postHint = postHint;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setNumComments(String numComments) {
        this.numComments = numComments;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
