package com.artem.nsu.redditfeed.api.json.post;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonFeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPostFeed extends JsonCommonFeed {

    @SerializedName("data")
    @Expose
    private JsonPostData data;

    public JsonPostFeed(String kind, JsonPostData data) {
        super(kind);
        this.data = data;
    }

    public JsonPostData getData() {
        return data;
    }

    public void setData(JsonPostData data) {
        this.data = data;
    }
}
