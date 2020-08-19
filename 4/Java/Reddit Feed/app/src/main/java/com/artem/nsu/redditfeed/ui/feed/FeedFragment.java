package com.artem.nsu.redditfeed.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.artem.nsu.redditfeed.MainActivity;
import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.FragmentFeedBinding;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.ui.post.IPostClickCallback;
import com.artem.nsu.redditfeed.ui.post.IThumbnailClickCallback;
import com.artem.nsu.redditfeed.ui.post.SwipeController;
import com.artem.nsu.redditfeed.ui.post.SwipeControllerActions;
import com.artem.nsu.redditfeed.viewmodel.FeedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment";
    private static final String KEY_SUBREDDIT_NAME = "subreddit_name";

    private FeedViewModel mViewModel;
    private FeedAdapter mFeedAdapter;
    private FragmentFeedBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        mFeedAdapter = new FeedAdapter(mPostClickCallback, mThumbnailClickCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.feedRecyclerView.setAdapter(mFeedAdapter);
        mBinding.feedRecyclerView.setLayoutManager(linearLayoutManager);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String subredditKey;
        if (getArguments() == null) {
            subredditKey = "popular";
        } else {
            subredditKey = requireArguments().getString(KEY_SUBREDDIT_NAME);
        }
        FeedViewModel.Factory factory = new FeedViewModel.Factory(
                requireActivity().getApplication(), subredditKey);
        mViewModel = new ViewModelProvider(this, factory).get(FeedViewModel.class);
        setRecyclerViewController();
        mBinding.feedSwipeRefresh.setOnRefreshListener(() -> {
            if (isNetworkAvailable()) {
                mViewModel.refresh();
            } else {
                mBinding.feedSwipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Failed to load content\nCheck your internet connection", Toast.LENGTH_LONG).show();
            }
        });
        subscribeUi(mViewModel.getPosts(), mViewModel.getIsPostsRefreshing());
    }

    private void subscribeUi(LiveData<PagedList<PostEntity>> liveData, LiveData<Boolean> isRefreshing) {
        liveData.observe(getViewLifecycleOwner(), mFeedAdapter::submitList);
        isRefreshing.observe(getViewLifecycleOwner(), isDataRefreshing -> mBinding.feedSwipeRefresh.setRefreshing(isDataRefreshing));
        mBinding.executePendingBindings();
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mFeedAdapter = null;
        super.onDestroyView();
    }

    private void setRecyclerViewController() {
        SwipeController swipeController = new SwipeController();
        swipeController.setButtonsActions(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                String subject = mFeedAdapter.getCurrentList().get(position).getTitle();
                String text = mFeedAdapter.getCurrentList().get(position).getComments();
                sendEmailIntent(subject, text);
            }

            @Override
            public void onLeftClicked(int position) {
                if (mFeedAdapter.getCurrentList() != null) {
                    if (mFeedAdapter.getCurrentList().get(position) != null) {
                        PostEntity currentPost = mFeedAdapter.getCurrentList().get(position);
                        if (currentPost.isFavorite()) {
                            currentPost.setFavorite(false);
                        } else {
                            currentPost.setFavorite(true);
                        }
                        mViewModel.updatePost(currentPost);
                    }
                }

            }
        });

        mBinding.feedRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(mBinding.feedRecyclerView);
    }

    private void sendEmailIntent(String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:abc@xyz.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getContext(), "Can't share e-mail", Toast.LENGTH_SHORT).show();
        }
    }

    // Not used in current app version, sending e-mail used instead
    private void sendShareIntent(String subject, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SENDTO);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(sendIntent);
        } else {
            Toast.makeText(getContext(), "Can't share data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static FeedFragment forSubreddit(String subreddit) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SUBREDDIT_NAME, subreddit);
        feedFragment.setArguments(args);
        return feedFragment;
    }

    private final IPostClickCallback mPostClickCallback = post -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showPostFragment(post);
        }
    };

    private final IThumbnailClickCallback mThumbnailClickCallback = post -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showContent(post);
        }
    };

}
