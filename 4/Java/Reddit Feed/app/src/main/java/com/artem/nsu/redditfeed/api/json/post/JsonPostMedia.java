package com.artem.nsu.redditfeed.api.json.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPostMedia {

    @SerializedName("reddit_video")
    @Expose
    private JsonPostVideo postVideo;

    public JsonPostMedia(JsonPostVideo postVideo) {
        this.postVideo = postVideo;
    }

    public JsonPostVideo getPostVideo() {
        return postVideo;
    }

}
