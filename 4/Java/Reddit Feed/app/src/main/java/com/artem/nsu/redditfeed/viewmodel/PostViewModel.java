package com.artem.nsu.redditfeed.viewmodel;


import android.app.Application;

import com.artem.nsu.redditfeed.RedditApp;
import com.artem.nsu.redditfeed.db.entity.CommentEntity;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.repository.DataRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

public class PostViewModel extends AndroidViewModel {

    private static final String TAG = "PostViewModel";

    private final String mPostId;
    private final String mCommentsUrl;

    private final LiveData<PostEntity> mObservablePost;
    private final LiveData<PagedList<CommentEntity>> mObservableComments;
    private final LiveData<Boolean> mIsCommentsRefreshing;

    public PostViewModel(@NonNull Application application, DataRepository repository,
                         final String postId, final String commentsUrl) {
        super(application);
        mPostId = postId;
        mCommentsUrl = commentsUrl;
        mObservablePost = repository.getPostById(postId);
        mIsCommentsRefreshing = repository.getIsCommentsRefreshing();
        mObservableComments = repository.getCommentsFromPost(postId, commentsUrl);
    }

    public LiveData<PostEntity> getObservablePost() {
        return mObservablePost;
    }

    public LiveData<PagedList<CommentEntity>> getObservableComments() {
        return mObservableComments;
    }

    public LiveData<Boolean> getIsCommentsRefreshing() {
        return mIsCommentsRefreshing;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mPostId;
        private final String mCommentsUrl;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, String postId, String commentsUrl) {
            mApplication = application;
            mPostId = postId;
            mCommentsUrl = commentsUrl;
            mRepository = ((RedditApp) application).getRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostViewModel(mApplication, mRepository, mPostId, mCommentsUrl);
        }
    }

}
