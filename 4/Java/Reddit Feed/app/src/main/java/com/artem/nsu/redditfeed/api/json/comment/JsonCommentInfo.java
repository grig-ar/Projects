package com.artem.nsu.redditfeed.api.json.comment;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonCommentInfo extends JsonCommonInfo {

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("link_id")
    @Expose
    private String postId;


    public JsonCommentInfo(String id, String author, String sub, String score,
                           String permaLink, String created, String body, String postId) {
        super(id, author, sub, score, permaLink, created);
        this.body = body;
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
