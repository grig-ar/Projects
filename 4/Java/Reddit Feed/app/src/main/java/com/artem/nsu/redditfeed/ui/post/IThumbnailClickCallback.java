package com.artem.nsu.redditfeed.ui.post;

import com.artem.nsu.redditfeed.model.IPost;

public interface IThumbnailClickCallback {
    void onImageClick(IPost post);
}
