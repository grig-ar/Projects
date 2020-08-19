package com.artem.nsu.redditfeed.api.json.post;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPostInfo extends JsonCommonInfo {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("selftext")
    @Expose
    private String text;

    @SerializedName("thumbnail")
    @Expose
    private String image;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("secure_media")
    @Expose
    private JsonPostMedia media;

    @SerializedName("num_comments")
    @Expose
    private String numComments;

    @SerializedName("post_hint")
    @Expose
    private String postHint;

    public JsonPostInfo(String title, String id, String author, String sub, String image, String url,
                        String score, String permaLink, String created, String text, String numComments,
                        String postHint) {
        super(id, author, sub, score, permaLink, created);
        this.title = title;
        this.text = text;
        this.image = image;
        this.url = url;
        this.numComments = numComments;
        this.postHint = postHint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNumComments() {
        return numComments;
    }

    public void setNumComments(String numComments) {
        this.numComments = numComments;
    }

    public String getPostHint() {
        return postHint;
    }

    public void setPostHint(String postHint) {
        this.postHint = postHint;
    }

    public JsonPostMedia getMedia() {
        return media;
    }
}
