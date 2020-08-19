package com.artem.nsu.redditfeed.ui.post;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.ItemCommentBinding;
import com.artem.nsu.redditfeed.db.entity.CommentEntity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends PagedListAdapter<CommentEntity, CommentAdapter.CommentViewHolder> {

    public CommentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<CommentEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CommentEntity>() {
                @Override
                public boolean areItemsTheSame(CommentEntity oldComment, CommentEntity newComment) {
                    return TextUtils.equals(
                            newComment.getId(), oldComment.getId());
                }

                @Override
                public boolean areContentsTheSame(CommentEntity oldComment, CommentEntity newComment) {
                    return TextUtils.equals(oldComment.getId(), newComment.getId())
                            && TextUtils.equals(oldComment.getCreated(), newComment.getCreated())
                            && TextUtils.equals(oldComment.getAuthor(), newComment.getAuthor())
                            && TextUtils.equals(oldComment.getBody(), newComment.getBody())
                            && TextUtils.equals(oldComment.getPostId(), newComment.getPostId())
                            && TextUtils.equals(oldComment.getScore(), newComment.getScore());
                }
            };

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_comment,
                        parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentEntity comment = getItem(position);
        if (comment != null) {
            holder.bindTo(comment);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }

        holder.binding.executePendingBindings();

    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        final ItemCommentBinding binding;
        private CommentEntity commentEntity = null;

        CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(CommentEntity comment) {
            commentEntity = comment;
            binding.setComment(comment);
        }

        void clear() {
            commentEntity = null;
        }
    }

}
