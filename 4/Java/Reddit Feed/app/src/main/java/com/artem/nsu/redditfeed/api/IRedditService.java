package com.artem.nsu.redditfeed.api;

import com.artem.nsu.redditfeed.api.json.comment.JsonCommentFeed;
import com.artem.nsu.redditfeed.api.json.post.JsonPostFeed;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRedditService {

    @Headers("Content-Type: application/json")
    @GET("{sub}/.json")
    Call<JsonPostFeed> getPosts(@Path("sub") String sub, @Query("limit") int limit, @Query("after") String after);

    @Headers("Content-Type: application/json")
    @GET("{comments}.json")
    Call<List<JsonCommentFeed>> getCommentsList(@Path("comments") String comments);

}
