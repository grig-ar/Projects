package com.artem.nsu.redditfeed.api.json.commons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonCommonData implements Serializable {

    @SerializedName("modhash")
    @Expose
    private String modHash;

    @SerializedName("dist")
    @Expose
    private String dist;

    @SerializedName("after")
    @Expose
    private String after;

    @SerializedName("before")
    @Expose
    private String before;

    public JsonCommonData(String modHash, String dist, String after, String before) {
        this.modHash = modHash;
        this.dist = dist;
        this.after = after;
        this.before = before;
    }

    public String getModHash() {
        return modHash;
    }

    public void setModHash(String modHash) {
        this.modHash = modHash;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

}
