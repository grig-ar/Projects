package com.artem.nsu.redditfeed.api.json.comment;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonEntry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonCommentEntry extends JsonCommonEntry {

    @SerializedName("data")
    @Expose
    private JsonCommentInfo commentInfo;

    public JsonCommentEntry(String kind, JsonCommentInfo commentInfo) {
        super(kind);
        this.commentInfo = commentInfo;
    }

    public JsonCommentInfo getCommentInfo() {
        return commentInfo;
    }

}
