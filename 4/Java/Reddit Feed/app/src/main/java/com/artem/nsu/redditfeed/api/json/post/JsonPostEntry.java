package com.artem.nsu.redditfeed.api.json.post;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonEntry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPostEntry extends JsonCommonEntry {

    @SerializedName("data")
    @Expose
    private JsonPostInfo postInfo;

    public JsonPostEntry(String kind, JsonPostInfo postInfo) {
        super(kind);
        this.postInfo = postInfo;
    }

    public JsonPostInfo getPostInfo() {
        return postInfo;
    }

}
