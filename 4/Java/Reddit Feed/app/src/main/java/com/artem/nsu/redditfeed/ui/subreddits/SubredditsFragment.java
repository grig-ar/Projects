package com.artem.nsu.redditfeed.ui.subreddits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.artem.nsu.redditfeed.MainActivity;
import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.FragmentSubredditsBinding;
import com.artem.nsu.redditfeed.model.ISubreddit;
import com.artem.nsu.redditfeed.viewmodel.SubredditsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SubredditsFragment extends Fragment {

    public static final String TAG = "SubredditsFragment";
    private SubredditAdapter mSubredditAdapter;
    private FragmentSubredditsBinding mBinding;

    private final ISubredditClickCallback mSubredditClickCallback = this::showFeed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subreddits, container, false);

        mSubredditAdapter = new SubredditAdapter(mSubredditClickCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mBinding.subredditsRecyclerView.setAdapter(mSubredditAdapter);
        mBinding.subredditsRecyclerView.setLayoutManager(linearLayoutManager);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final SubredditsViewModel viewModel = new ViewModelProvider(this).get(SubredditsViewModel.class);

        mBinding.searchButton.setOnClickListener(v -> {
            String subreddit = mBinding.searchEt.getText().toString();
            if (!subreddit.equals("")) {
                showFeed(subreddit);
            }
        });

        mBinding.searchEt.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showFeed(v.getText().toString());
                handled = true;
            }
            return handled;
        });
        subscribeUi(viewModel.getSubreddits());
    }

    private void subscribeUi(LiveData<List<ISubreddit>> liveData) {
        liveData.observe(getViewLifecycleOwner(), subreddits -> {
            if (subreddits != null) {
                mSubredditAdapter.setSubredditList(subreddits);
            }
        });
    }

    private void showFeed(String subreddit) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showFeedFragment(subreddit);
        }
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }

}
