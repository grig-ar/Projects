package com.artem.nsu.redditfeed.db;

import android.content.Context;

import com.artem.nsu.redditfeed.AppExecutors;
import com.artem.nsu.redditfeed.db.dao.ICommentDao;
import com.artem.nsu.redditfeed.db.dao.IPostDao;
import com.artem.nsu.redditfeed.db.entity.CommentEntity;
import com.artem.nsu.redditfeed.db.entity.PostEntity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PostEntity.class, CommentEntity.class}, version = 4)
public abstract class RedditDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "reddit_database";
    private static volatile RedditDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private AppExecutors mAppExecutors;

    public abstract ICommentDao commentDao();

    public abstract IPostDao postDao();

    public static RedditDatabase getInstance(final Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (RedditDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            RedditDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    sInstance.setDatabaseCreated();
                    sInstance.setExecutors(executors);
                }
            }
        }
        return sInstance;
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private void setExecutors(AppExecutors executors) {
        mAppExecutors = executors;
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }

}
