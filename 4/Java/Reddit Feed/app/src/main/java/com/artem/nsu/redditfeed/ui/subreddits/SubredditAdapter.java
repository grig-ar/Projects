package com.artem.nsu.redditfeed.ui.subreddits;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.artem.nsu.redditfeed.R;
import com.artem.nsu.redditfeed.databinding.ItemSubredditBinding;
import com.artem.nsu.redditfeed.model.ISubreddit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubredditViewHolder> {

    private List<ISubreddit> mSubredditList;

    @Nullable
    private final ISubredditClickCallback mSubredditClickCallback;

    public SubredditAdapter(@Nullable ISubredditClickCallback clickCallback) {
        mSubredditClickCallback = clickCallback;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public SubredditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubredditBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_subreddit,
                        parent, false);
        binding.setCallback(mSubredditClickCallback);
        return new SubredditViewHolder(binding);
    }

    public void setSubredditList(final List<ISubreddit> subredditList) {
        if (mSubredditList == null) {
            mSubredditList = subredditList;
            notifyItemRangeInserted(0, subredditList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mSubredditList.size();
                }

                @Override
                public int getNewListSize() {
                    return subredditList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TextUtils.equals(
                            subredditList.get(newItemPosition).getId(), mSubredditList.get(oldItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ISubreddit newSubreddit = subredditList.get(newItemPosition);
                    ISubreddit oldSubreddit = mSubredditList.get(oldItemPosition);
                    return TextUtils.equals(newSubreddit.getId(), oldSubreddit.getId())
                            && TextUtils.equals(newSubreddit.getUrl(), oldSubreddit.getUrl())
                            && TextUtils.equals(newSubreddit.getThumbnailUrl(), oldSubreddit.getThumbnailUrl())
                            && TextUtils.equals(newSubreddit.getName(), oldSubreddit.getName());
                }
            });
            mSubredditList = subredditList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditViewHolder holder, int position) {
        holder.binding.setSubreddit(mSubredditList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSubredditList == null ? 0 : mSubredditList.size();
    }

    public List<ISubreddit> getSubredditList() {
        return mSubredditList;
    }

    @Override
    public long getItemId(int position) {
        return mSubredditList.get(position).getId().hashCode();
    }

    static class SubredditViewHolder extends RecyclerView.ViewHolder {
        final ItemSubredditBinding binding;

        SubredditViewHolder(ItemSubredditBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
