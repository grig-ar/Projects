package com.artem.nsu.redditfeed.db.dao;

import com.artem.nsu.redditfeed.db.entity.CommentEntity;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ICommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CommentEntity commentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<CommentEntity> commentEntityList);

    @Update
    void update(CommentEntity commentEntity);

    @Delete
    void delete(CommentEntity commentEntity);

    @Query("DELETE FROM comment_table")
    void deleteAllComments();

    @Query("SELECT * FROM comment_table")
    DataSource.Factory<Integer, CommentEntity> getAllComments();

    @Query("SELECT COUNT(*) FROM comment_table")
    Integer getCommentsListCount();

    @Query("SELECT * FROM comment_table WHERE postId = :id")
    DataSource.Factory<Integer, CommentEntity> getCommentsByPostId(String id);

    @Query("SELECT COUNT(*) FROM comment_table WHERE postId = :id")
    Integer getCommentsCountByPostId(String id);

}
