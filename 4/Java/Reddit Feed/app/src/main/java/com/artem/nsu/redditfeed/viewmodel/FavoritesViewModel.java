package com.artem.nsu.redditfeed.viewmodel;

import android.app.Application;

import com.artem.nsu.redditfeed.RedditApp;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.repository.DataRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<PagedList<PostEntity>> mPosts;
    private final DataRepository mRepository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((RedditApp) application).getRepository();
        mPosts = mRepository.getPagedFavoritePostsList();

    }

    public void refresh() {
        mPosts = mRepository.getPagedFavoritePostsList();
    }

    public void updatePost(PostEntity post) {
        mRepository.updatePost(post);
    }

    public LiveData<PagedList<PostEntity>> getPosts() {
        return mPosts;
    }

}
