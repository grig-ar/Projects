package com.artem.nsu.redditfeed.db.dao;

import com.artem.nsu.redditfeed.db.entity.PostEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostEntity postEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<PostEntity> postEntityList);

    @Update
    void update(PostEntity postEntity);

    @Delete
    void delete(PostEntity postEntity);

    @Delete
    void deleteOldPosts(List<PostEntity> posts);

    @Query("SELECT * FROM post_table")
    List<PostEntity> getNonObservablePosts();

    @Query("DELETE FROM post_table")
    void deleteAllPosts();

    @Query("SELECT * FROM post_table WHERE addedTime > :expirationTime AND isPopular = 1")
    DataSource.Factory<Integer, PostEntity> getLastPopularPosts(long expirationTime);

    @Query("SELECT * FROM post_table WHERE addedTime > :expirationTime AND subreddit = :currentSubreddit")
    DataSource.Factory<Integer, PostEntity> getLastPostsFromSubreddit(long expirationTime, String currentSubreddit);

    @Query("SELECT * FROM post_table WHERE isFavorite = 1")
    DataSource.Factory<Integer, PostEntity> getFavoritePosts();

    @Query("DELETE FROM post_table WHERE isFavorite = 0")
    void deleteNotFavoritePosts();

    @Query("SELECT COUNT(*) FROM post_table")
    Integer getPostListCount();

    @Query("SELECT * FROM post_table where id = :postId")
    LiveData<PostEntity> loadPost(String postId);

    @Query("SELECT * FROM post_table where id = :postId")
    PostEntity getNonObservablePost(String postId);

    @Query("SELECT * FROM post_table WHERE subreddit = :subreddit AND addedTime > :expirationTime")
    DataSource.Factory<Integer, PostEntity> getPostsFromSubreddit(String subreddit, long expirationTime);

}
