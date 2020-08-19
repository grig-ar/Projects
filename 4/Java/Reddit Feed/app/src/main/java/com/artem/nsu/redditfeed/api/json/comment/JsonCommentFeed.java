package com.artem.nsu.redditfeed.api.json.comment;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonFeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonCommentFeed extends JsonCommonFeed {

    @SerializedName("data")
    @Expose
    private JsonCommentData data;

    public JsonCommentFeed(String kind, JsonCommentData data) {
        super(kind);
        this.data = data;
    }

    public JsonCommentData getData() {
        return data;
    }

    public void setData(JsonCommentData data) {
        this.data = data;
    }

}
