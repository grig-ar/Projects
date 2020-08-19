package com.artem.nsu.redditfeed.ui.fullscreencontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.FragmentFullscreenContentBinding;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.model.IPost;
import com.artem.nsu.redditfeed.viewmodel.FullscreenContentViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

public class FullscreenContentFragment extends Fragment {

    private static final String KEY_POST_ID = "post_id";
    private static final String TAG = "FullscreenContentFragment";

    private FragmentFullscreenContentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fullscreen_content, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FullscreenContentViewModel.Factory factory = new FullscreenContentViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getString(KEY_POST_ID));
        final FullscreenContentViewModel viewModel = new ViewModelProvider(this, factory)
                .get(FullscreenContentViewModel.class);
        mBinding.setIsLoading(true);
        subscribeUi(viewModel.getPostId());
    }

    private void subscribeUi(LiveData<PostEntity> liveData) {
        liveData.observe(getViewLifecycleOwner(), post -> {
            if (post != null) {
                mBinding.setPost(post);
                mBinding.setIsLoading(false);
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }

    public static FullscreenContentFragment forContent(IPost post) {
        FullscreenContentFragment contentFragment = new FullscreenContentFragment();
        Bundle args = new Bundle();
        args.putString(KEY_POST_ID, post.getId());
        contentFragment.setArguments(args);
        return contentFragment;
    }

}
