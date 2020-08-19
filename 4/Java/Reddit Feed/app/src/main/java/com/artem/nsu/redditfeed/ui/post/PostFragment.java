package com.artem.nsu.redditfeed.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artem.nsu.redditfeed.MainActivity;
import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.FragmentPostBinding;
import com.artem.nsu.redditfeed.viewmodel.PostViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PostFragment extends Fragment {

    private static final String KEY_PRODUCT_ID = "post_id";
    private static final String KEY_COMMENTS_URL = "comments_url";

    private FragmentPostBinding mBinding;
    private CommentAdapter mCommentAdapter;

    private final IFabClickCallback mFabClickCallback = post -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showContent(post);
        }
    };

    private final IThumbnailClickCallback mThumbnailClickCallback = post -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showContent(post);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mCommentAdapter = new CommentAdapter();
        mBinding.setFabCallback(mFabClickCallback);
        mBinding.commentsRecyclerView.setAdapter(mCommentAdapter);
        mBinding.commentsRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.commentsScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                mBinding.fabExternal.hide();
            } else {
                mBinding.fabExternal.show();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PostViewModel.Factory factory = new PostViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getString(KEY_PRODUCT_ID),
                requireArguments().getString(KEY_COMMENTS_URL));

        final PostViewModel model = new ViewModelProvider(this, factory)
                .get(PostViewModel.class);

        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setPostViewModel(model);

        subscribeUi(model);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void subscribeUi(final PostViewModel model) {
        model.getObservablePost().observe(getViewLifecycleOwner(), postEntity -> {
            if (postEntity != null) {
                mBinding.setPost(postEntity);
                mBinding.incItemPost.setPost(postEntity);
                mBinding.incItemPost.setIsLoading(false);
                mBinding.incItemPost.setThumbnailCallback(mThumbnailClickCallback);
                mBinding.incItemPost.cardImage.setVisibility(View.VISIBLE);
            } else {
                mBinding.incItemPost.setIsLoading(true);
            }
        });

        model.getIsCommentsRefreshing().observe(getViewLifecycleOwner(),
                isCommentsRefreshing -> mBinding.setIsLoading(isCommentsRefreshing));

        model.getObservableComments().observe(getViewLifecycleOwner(), commentEntities -> {
            if (commentEntities != null) {
                mCommentAdapter.submitList(commentEntities);
            }
        });
    }

    public static PostFragment forPost(String postId, String commentsUrl) {
        PostFragment postFragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PRODUCT_ID, postId);
        args.putString(KEY_COMMENTS_URL, commentsUrl);
        postFragment.setArguments(args);
        return postFragment;
    }

}
