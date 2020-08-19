package com.artem.nsu.redditfeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.artem.nsu.redditfeed.databinding.ActivityMainBinding;
import com.artem.nsu.redditfeed.model.IPost;
import com.artem.nsu.redditfeed.ui.favorites.FavoritesFragment;
import com.artem.nsu.redditfeed.ui.feed.FeedFragment;
import com.artem.nsu.redditfeed.ui.fullscreencontent.FullscreenContentFragment;
import com.artem.nsu.redditfeed.ui.post.PostFragment;
import com.artem.nsu.redditfeed.ui.subreddits.SubredditsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String DEFAULT_SUBREDDIT_NAME = "popular";
    private static final String NAME_SUBREDDIT_FRAGMENT = "subreddit";
    private static final String NAME_FEED_FRAGMENT = "feed";
    private static final String NAME_POST_FRAGMENT = "post";
    private static final String NAME_CONTENT_FRAGMENT = "content";
    private static final String NAME_FAVORITES_FRAGMENT = "favorites";

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
        if (savedInstanceState == null) {
            showFeedFragment(DEFAULT_SUBREDDIT_NAME);
        }
    }

    @Override
    public void onBackPressed() {
        int selectedId = mBottomNavigationView.getSelectedItemId();
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (getSupportFragmentManager().getBackStackEntryAt(index).getName().equals(NAME_POST_FRAGMENT)
                || getSupportFragmentManager().getBackStackEntryAt(index).getName().equals(NAME_CONTENT_FRAGMENT)) {
            super.onBackPressed();
            return;
        }
        if (selectedId != R.id.nav_feed) {
            mBottomNavigationView.setSelectedItemId(R.id.nav_feed);
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryAt(index).getName().equals(NAME_FEED_FRAGMENT)) {
            finish();
            return;
        }
        super.onBackPressed();
    }


    public void showSubredditsFragment() {
        SubredditsFragment fragment = new SubredditsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(NAME_SUBREDDIT_FRAGMENT)
                .replace(R.id.fragment_container, fragment, FeedFragment.TAG)
                .commit();
    }

    public void showPostFragment(IPost post) {
        PostFragment postFragment = PostFragment.forPost(post.getId(), post.getComments());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(NAME_POST_FRAGMENT)
                .replace(R.id.fragment_container, postFragment, null)
                .commit();
    }

    public void showFeedFragment(String subreddit) {
        FeedFragment feedFragment = FeedFragment.forSubreddit(subreddit);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(NAME_FEED_FRAGMENT)
                .replace(R.id.fragment_container, feedFragment, null)
                .commit();
    }

    public void showFavoritesFragment() {
        FavoritesFragment favoritesFragment = new FavoritesFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(NAME_FAVORITES_FRAGMENT)
                .replace(R.id.fragment_container, favoritesFragment, null)
                .commit();
    }

    public void showContent(IPost post) {
        if (post.getPostHint() == null) {
            openWebPage(post.getUrl());
            return;
        }
        switch (post.getPostHint()) {
            case "image":
                FullscreenContentFragment contentFragment = FullscreenContentFragment.forContent(post);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(NAME_CONTENT_FRAGMENT)
                        .replace(R.id.fragment_container, contentFragment, null)
                        .commit();
                break;
            case "hosted:video":
                openWebPage(post.getVideoUrl());
                break;
            case "link":
            case "rich:video":
            default:
                openWebPage(post.getUrl());
                break;
        }
    }

    public void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        showFeedFragment(DEFAULT_SUBREDDIT_NAME);
                        break;
                    case R.id.nav_subreddits:
                        showSubredditsFragment();
                        break;
                    case R.id.nav_favorites:
                        showFavoritesFragment();
                        break;
                    default:
                        return false;
                }
                return true;
            };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
