package com.artem.nsu.redditfeed.ui.favorites;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.artem.nsu.redditfeed.MainActivity;
import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.FragmentFavoritesBinding;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.ui.feed.FeedAdapter;
import com.artem.nsu.redditfeed.ui.post.IPostClickCallback;
import com.artem.nsu.redditfeed.ui.post.IThumbnailClickCallback;
import com.artem.nsu.redditfeed.ui.post.SwipeController;
import com.artem.nsu.redditfeed.ui.post.SwipeControllerActions;
import com.artem.nsu.redditfeed.viewmodel.FavoritesViewModel;

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

public class FavoritesFragment extends Fragment {

    public static final String TAG = "FavoritesFragment";

    private FeedAdapter mFavoritesAdapter;
    private FavoritesViewModel mViewModel;
    private FragmentFavoritesBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        mFavoritesAdapter = new FeedAdapter(mPostClickCallback, mThumbnailClickCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.favoritesRecyclerView.setAdapter(mFavoritesAdapter);
        mBinding.favoritesRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) requireActivity()).showFavoritesFragment();
            }
        });
        setRecyclerViewController();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        subscribeUi(mViewModel.getPosts());
    }

    private void subscribeUi(LiveData<PagedList<PostEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), mFavoritesAdapter::submitList);
        mBinding.executePendingBindings();
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mFavoritesAdapter = null;
        super.onDestroyView();
    }

    private void setRecyclerViewController() {
        SwipeController swipeController = new SwipeController();
        swipeController.setButtonsActions(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                String subject = mFavoritesAdapter.getCurrentList().get(position).getTitle();
                String text = mFavoritesAdapter.getCurrentList().get(position).getComments();
                sendEmailIntent(subject, text);
            }

            @Override
            public void onLeftClicked(int position) {
                if (mFavoritesAdapter.getCurrentList() != null) {
                    if (mFavoritesAdapter.getCurrentList().get(position) != null) {
                        PostEntity currentPost = mFavoritesAdapter.getCurrentList().get(position);
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

        mBinding.favoritesRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(mBinding.favoritesRecyclerView);
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

