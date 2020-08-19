package com.artem.nsu.redditfeed.api.json.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPostVideo {

    @SerializedName("fallback_url")
    @Expose
    private String videoUrl;

    public JsonPostVideo(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

}
