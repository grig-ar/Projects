package com.artem.nsu.redditfeed.binding;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.artem.nsu.redditfeed.util.ColorFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void showHide(@NonNull View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(
            value = {"imageUrl", "imageRequestListener"},
            requireAll = false
    )
    public static void bindImage(@NonNull ImageView imageView, @Nullable String url, @Nullable RequestListener<Drawable> listener) {
        if (url == null
                || TextUtils.equals(url, "default")
                || TextUtils.equals(url, "nsfw")
                || TextUtils.equals(url, "self")
                || TextUtils.equals(url, "")
        ) {
            imageView.setVisibility(View.GONE);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .listener(listener)
                .error(new ColorDrawable(ColorFactory.getErrorColor()))
                .placeholder(new ColorDrawable(ColorFactory.getPlaceholderColor()))
                .into(imageView);
    }

    @BindingAdapter(
            value = {"thumbnailUrl", "imageRequestListener"},
            requireAll = false
    )
    public static void bindThumbnail(@NonNull ImageView imageView, @Nullable String thumbnailUrl, @Nullable RequestListener<Drawable> listener) {
        if (thumbnailUrl == null) {
            Glide.with(imageView.getContext())
                    .load(new ColorDrawable(ColorFactory.getPlaceholderColor()))
                    .apply(RequestOptions.circleCropTransform())
                    .listener(listener)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(thumbnailUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .listener(listener)
                    .error(new ColorDrawable(ColorFactory.getErrorColor()))
                    .placeholder(new ColorDrawable(ColorFactory.getPlaceholderColor()))
                    .into(imageView);
        }
    }

}
