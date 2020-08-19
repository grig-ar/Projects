package com.artem.nsu.redditfeed.api.json.commons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonCommonFeed implements Serializable {

    @SerializedName("kind")
    @Expose
    private String kind;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public JsonCommonFeed(String kind) {
        this.kind = kind;
    }
}
