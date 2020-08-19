package com.artem.nsu.redditfeed.viewmodel;

import android.app.Application;

import com.artem.nsu.redditfeed.RedditApp;
import com.artem.nsu.redditfeed.db.entity.PostEntity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FullscreenContentViewModel extends AndroidViewModel {
    private final LiveData<PostEntity> mObservablePost;

    public FullscreenContentViewModel(@NonNull Application application, String postId) {
        super(application);
        mObservablePost = ((RedditApp) application).getRepository().getPostById(postId);
    }

    public LiveData<PostEntity> getPostId() {
        return mObservablePost;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mPostId;

        public Factory(@NonNull Application application, String postId) {
            mApplication = application;
            mPostId = postId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FullscreenContentViewModel(mApplication, mPostId);
        }
    }

}
