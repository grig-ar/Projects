package com.artem.nsu.redditfeed.viewmodel;

import android.app.Application;

import com.artem.nsu.redditfeed.RedditApp;
import com.artem.nsu.redditfeed.model.ISubreddit;
import com.artem.nsu.redditfeed.repository.DataRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SubredditsViewModel extends AndroidViewModel {

    private final DataRepository mRepository;
    private final MutableLiveData<List<ISubreddit>> mSubreddits = new MutableLiveData<>();


    public SubredditsViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((RedditApp) application).getRepository();
        mSubreddits.postValue(mRepository.getSubreddits());
    }

    public LiveData<List<ISubreddit>> getSubreddits() {
        return mSubreddits;
    }
}
