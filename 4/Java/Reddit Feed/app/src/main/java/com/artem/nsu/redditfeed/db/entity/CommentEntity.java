package com.artem.nsu.redditfeed.db.entity;

import com.artem.nsu.redditfeed.api.json.comment.JsonCommentEntry;
import com.artem.nsu.redditfeed.api.json.comment.JsonCommentInfo;
import com.artem.nsu.redditfeed.model.IComment;
import com.artem.nsu.redditfeed.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "comment_table",
        foreignKeys = {
                @ForeignKey(entity = PostEntity.class,
                        parentColumns = "id",
                        childColumns = "postId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)},
        indices = {@Index(value = "postId")
        })
public class CommentEntity implements IComment {

    @PrimaryKey
    @NonNull
    private String id;

    private String postId;

    private String author;

    private String body;

    private String created;

    private String score;

    public CommentEntity(@NonNull String id, String author, String body,
                         String created, String score, String postId) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.created = created;
        this.score = score;
        this.postId = postId;
    }

    @Ignore
    public static List<CommentEntity> createCommentsListFromEntries(List<JsonCommentEntry> entries) {
        List<CommentEntity> commentsList = new ArrayList<>();
        for (JsonCommentEntry entry : entries) {
            if (entry.getKind().equals("more")) {
                continue;
            }
            JsonCommentInfo info = entry.getCommentInfo();
            long epochTime = (long) Math.round(Float.parseFloat(info.getCreated())) * 1000L;
            commentsList.add(new CommentEntity(info.getId(), info.getAuthor(), info.getBody(),
                    DateFormatter.formatEpochTime(epochTime), info.getScore(), info.getPostId()));
        }

        return commentsList;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getCreated() {
        return created;
    }

    public String getScore() {
        return score;
    }

    public String getPostId() {
        return postId;
    }
}
