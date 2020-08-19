package com.artem.nsu.redditfeed.api.json.comment;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonCommentData extends JsonCommonData {

    @SerializedName("children")
    @Expose
    private ArrayList<JsonCommentEntry> entries;

    public JsonCommentData(String modHash, String dist, String after, String before, ArrayList<JsonCommentEntry> entries) {
        super(modHash, dist, after, before);
        this.entries = entries;
    }

    public ArrayList<JsonCommentEntry> getEntries() {
        return entries;
    }

}
