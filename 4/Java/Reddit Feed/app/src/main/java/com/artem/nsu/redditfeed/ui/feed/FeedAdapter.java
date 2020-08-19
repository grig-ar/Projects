package com.artem.nsu.redditfeed.ui.feed;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.ItemPostBinding;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.ui.post.IPostClickCallback;
import com.artem.nsu.redditfeed.ui.post.IThumbnailClickCallback;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends PagedListAdapter<PostEntity, FeedAdapter.PostViewHolder> {

    @Nullable
    private final IPostClickCallback mPostClickCallback;

    @Nullable
    private final IThumbnailClickCallback mThumbnailClickCallback;

    public FeedAdapter(@Nullable IPostClickCallback clickCallback, @Nullable IThumbnailClickCallback thumbnailClickCallback) {
        super(DIFF_CALLBACK);
        mPostClickCallback = clickCallback;
        mThumbnailClickCallback = thumbnailClickCallback;
    }

    private static DiffUtil.ItemCallback<PostEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PostEntity>() {

                @Override
                public boolean areItemsTheSame(PostEntity oldPost, PostEntity newPost) {
                    return TextUtils.equals(
                            newPost.getId(), oldPost.getId());
                }

                @Override
                public boolean areContentsTheSame(PostEntity oldPost, PostEntity newPost) {
                    return TextUtils.equals(newPost.getId(), oldPost.getId())
                            && TextUtils.equals(newPost.getUrl(), oldPost.getUrl())
                            && TextUtils.equals(newPost.getCreated(), oldPost.getCreated())
                            && TextUtils.equals(newPost.getComments(), oldPost.getComments())
                            && TextUtils.equals(newPost.getTitle(), oldPost.getTitle())
                            && TextUtils.equals(newPost.getScore(), oldPost.getScore())
                            && TextUtils.equals(newPost.getAuthor(), oldPost.getAuthor())
                            && TextUtils.equals(newPost.getSubreddit(), oldPost.getSubreddit())
                            && TextUtils.equals(newPost.getPostHint(), oldPost.getPostHint())
                            && TextUtils.equals(newPost.getNumComments(), oldPost.getNumComments())
                            && TextUtils.equals(newPost.getText(), oldPost.getText())
                            && TextUtils.equals(newPost.getImage(), oldPost.getImage());
                }
            };

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post,
                        parent, false);
        binding.setIsLoading(true);
        binding.setPostCallback(mPostClickCallback);
        binding.setThumbnailCallback(mThumbnailClickCallback);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostEntity post = getItem(position);

        if (post != null) {
            holder.bindTo(post);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }

        holder.binding.executePendingBindings();

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        final ItemPostBinding binding;
        private PostEntity postEntity = null;

        PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(PostEntity post) {
            postEntity = post;
            binding.setPost(post);
            String url = post.getImage();
            if (url == null
                    || TextUtils.equals(url, "default")
                    || TextUtils.equals(url, "nsfw")
                    || TextUtils.equals(url, "self")
                    || TextUtils.equals(url, "")
            ) {
                binding.setIsLoading(false);
            }

            binding.setImageRequestListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binding.setIsLoading(false);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    binding.setIsLoading(false);
                    return false;
                }
            });
        }

        void clear() {
            postEntity = null;
        }

        public PostEntity getPostEntity() {
            return postEntity;
        }
    }
}

