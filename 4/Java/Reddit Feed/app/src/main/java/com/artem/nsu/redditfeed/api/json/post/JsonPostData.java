package com.artem.nsu.redditfeed.api.json.post;

import com.artem.nsu.redditfeed.api.json.commons.JsonCommonData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class JsonPostData extends JsonCommonData {

    @SerializedName("children")
    @Expose
    private ArrayList<JsonPostEntry> entries;

    public JsonPostData(String modHash, String dist, String after, String before, ArrayList<JsonPostEntry> entries) {
        super(modHash, dist, after, before);
        this.entries = entries;
    }

    public ArrayList<JsonPostEntry> getEntries() {
        return entries;
    }

}
