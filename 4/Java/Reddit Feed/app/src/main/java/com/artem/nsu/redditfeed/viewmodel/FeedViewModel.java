package com.artem.nsu.redditfeed.viewmodel;

import android.app.Application;

import com.artem.nsu.redditfeed.RedditApp;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.repository.DataRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

public class FeedViewModel extends AndroidViewModel {

    private final DataRepository mRepository;
    private final LiveData<PagedList<PostEntity>> mPosts;
    private final LiveData<Boolean> mIsPostsRefreshing;

    public FeedViewModel(@NonNull Application application, String subreddit) {
        super(application);
        mRepository = ((RedditApp) application).getRepository();
        mPosts = mRepository.getPagedPostsList(subreddit);
        mIsPostsRefreshing = mRepository.getIsPostsRefreshing();
    }

    public void refresh() {
        mRepository.deleteNotFavoritePosts();
    }

    public void updatePost(PostEntity post) {
        mRepository.updatePost(post);
    }

    public LiveData<PagedList<PostEntity>> getPosts() {
        return mPosts;
    }

    public LiveData<Boolean> getIsPostsRefreshing() {
        return mIsPostsRefreshing;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final String mSubreddit;

        public Factory(@NonNull Application application, String subreddit) {
            mApplication = application;
            mSubreddit = subreddit;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FeedViewModel(mApplication, mSubreddit);
        }
    }

}
