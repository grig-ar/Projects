package com.artem.nsu.redditfeed.api.json.commons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonCommonEntry implements Serializable {

    @SerializedName("kind")
    @Expose
    private String kind;

    public JsonCommonEntry(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

}
