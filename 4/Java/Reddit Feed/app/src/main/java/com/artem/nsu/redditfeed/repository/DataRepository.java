package com.artem.nsu.redditfeed.repository;

import com.artem.nsu.redditfeed.AppExecutors;
import com.artem.nsu.redditfeed.api.IRedditService;
import com.artem.nsu.redditfeed.api.SubredditsProvider;
import com.artem.nsu.redditfeed.api.json.comment.JsonCommentFeed;
import com.artem.nsu.redditfeed.api.json.post.JsonPostFeed;
import com.artem.nsu.redditfeed.db.RedditDatabase;
import com.artem.nsu.redditfeed.db.dao.ICommentDao;
import com.artem.nsu.redditfeed.db.dao.IPostDao;
import com.artem.nsu.redditfeed.db.entity.CommentEntity;
import com.artem.nsu.redditfeed.db.entity.PostEntity;
import com.artem.nsu.redditfeed.model.ISubreddit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private static volatile DataRepository sInstance;

    private static final int PAGE_SIZE = 15;
    private static final long TIME_TO_EXPIRE = TimeUnit.HOURS.toMillis(12);
    private static final String BASE_URL = "https://www.reddit.com/r/";

    private final IRedditService mRedditApi;
    private final AppExecutors mAppExecutors;
    private final MutableLiveData<Boolean> mIsApiPostsRefreshing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsApiCommentsRefreshing = new MutableLiveData<>();
    private IPostDao mPostDao;
    private ICommentDao mCommentDao;
    private final List<ISubreddit> mSubreddits;

    private DataRepository(final RedditDatabase database, final IRedditService redditApi) {
        mRedditApi = redditApi;
        mPostDao = database.postDao();
        mCommentDao = database.commentDao();
        mSubreddits = SubredditsProvider.getSubreddits();
        mAppExecutors = database.getAppExecutors();

        // version to save content
        deleteOldPosts();

        // version to load new content when app launching
        //deleteNotFavoritePosts();
    }

    public static DataRepository getInstance(final RedditDatabase database, final IRedditService userApi) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, userApi);
                }
            }
        }
        return sInstance;
    }

    public MutableLiveData<Boolean> getIsPostsRefreshing() {
        return mIsApiPostsRefreshing;
    }

    public MutableLiveData<Boolean> getIsCommentsRefreshing() {
        return mIsApiCommentsRefreshing;
    }

    public LiveData<PagedList<PostEntity>> getPagedPostsList(String subreddit) {
        boolean isPopular = false;
        if (subreddit.equalsIgnoreCase("popular")) {
            isPopular = true;
        }
        if (isPopular) {
            return new LivePagedListBuilder<>(mPostDao
                    .getLastPopularPosts(System.currentTimeMillis() - TIME_TO_EXPIRE), PAGE_SIZE)
                    .setBoundaryCallback(new PagedList.BoundaryCallback<PostEntity>() {
                        @Override
                        public void onZeroItemsLoaded() {
                            super.onZeroItemsLoaded();
                            loadNextData(subreddit, null);
                        }


                        @Override
                        public void onItemAtEndLoaded(@NonNull PostEntity itemAtEnd) {
                            loadNextData(subreddit, itemAtEnd.getId());
                        }
                    }).build();
        } else {
            return new LivePagedListBuilder<>(mPostDao
                    .getLastPostsFromSubreddit(System.currentTimeMillis() - TIME_TO_EXPIRE, subreddit), PAGE_SIZE)
                    .setBoundaryCallback(new PagedList.BoundaryCallback<PostEntity>() {
                        @Override
                        public void onZeroItemsLoaded() {
                            super.onZeroItemsLoaded();
                            loadNextData(subreddit, null);
                        }


                        @Override
                        public void onItemAtEndLoaded(@NonNull PostEntity itemAtEnd) {
                            loadNextData(subreddit, itemAtEnd.getId());
                        }
                    }).build();
        }
    }

    public LiveData<PagedList<PostEntity>> getPagedFavoritePostsList() {
        return new LivePagedListBuilder<>(mPostDao.getFavoritePosts(), PAGE_SIZE).build();
    }

    public LiveData<PagedList<CommentEntity>> getPagedCommentsList() {
        return new LivePagedListBuilder<>(mCommentDao.getAllComments(), PAGE_SIZE).build();
    }

    public void insertPost(PostEntity post) {
        mAppExecutors.diskIO().execute(() -> mPostDao.insert(post));
    }

    public List<Long> insertAllPosts(List<PostEntity> posts) {
        Future<List<Long>> result = mAppExecutors.diskIO().submit(() -> mPostDao.insertAll(posts));
        List<Long> returnValue;
        try {
            returnValue = result.get();
        } catch (Exception e) {
            return null;
        }
        return returnValue;
    }

    public void updatePost(PostEntity post) {
        mAppExecutors.diskIO().execute(() -> mPostDao.update(post));
    }

    public void deletePost(PostEntity post) {
        mAppExecutors.diskIO().execute(() -> mPostDao.delete(post));
    }

    public void deleteAllPosts() {
        mAppExecutors.diskIO().execute(() -> mPostDao.deleteAllPosts());
    }

    // debugging method
    public PostEntity getNonObservablePost(String postId) {
        Future<PostEntity> result = mAppExecutors.diskIO().submit(() -> mPostDao.getNonObservablePost(postId));
        try {
            return result.get();
        } catch (Exception e) {
            return null;
        }
    }

    // debugging method
    public Integer getPostsListCount() {
        Future<Integer> result = mAppExecutors.diskIO()
                .submit(() -> mPostDao.getPostListCount());
        try {
            return result.get();
        } catch (Exception e) {
            return -1;
        }
    }

    // debugging method
    public Integer getCommentsCount(String postId) {
        Future<Integer> result = mAppExecutors.diskIO()
                .submit(() -> mCommentDao.getCommentsCountByPostId(postId));
        try {
            return result.get();
        } catch (Exception e) {
            return -1;
        }
    }

    public LiveData<PostEntity> getPostById(String id) {
        Future<LiveData<PostEntity>> result = mAppExecutors.diskIO().submit(() -> mPostDao.loadPost(id));
        try {
            return result.get();
        } catch (Exception e) {
            return null;
        }
    }

    public LiveData<PagedList<CommentEntity>> getCommentsById(String id) {
        return new LivePagedListBuilder<>(mCommentDao.getCommentsByPostId(id), PAGE_SIZE).build();
    }

    public void deleteNotFavoritePosts() {
        mAppExecutors.diskIO().submit(() -> mPostDao.deleteNotFavoritePosts());
    }

    public void deleteOldPosts() {
        Future<List<PostEntity>> result = mAppExecutors.diskIO().submit(() -> mPostDao.getNonObservablePosts());
        try {
            List<PostEntity> posts = result.get();
            if (posts.size() == 0) {
                return;
            }
            List<PostEntity> oldPosts = new ArrayList<>();
            for (PostEntity post : posts) {
                if (post.getAddedTime() < System.currentTimeMillis() - TIME_TO_EXPIRE) {
                    oldPosts.add(post);
                }
            }
            mAppExecutors.diskIO().submit(() -> mPostDao.deleteOldPosts(oldPosts));
        } catch (Exception ignored) {
        }
    }

    public void insertComment(CommentEntity comment) {
        mAppExecutors.diskIO().execute(() -> mCommentDao.insert(comment));
    }

    public List<Long> insertAllComments(List<CommentEntity> comments) {
        Future<List<Long>> result = mAppExecutors.diskIO().submit(() -> mCommentDao.insertAll(comments));
        try {
            return result.get();
        } catch (Exception e) {
            return null;
        }
    }

    public void updateComment(CommentEntity comment) {
        mAppExecutors.diskIO().execute(() -> mCommentDao.update(comment));
    }

    public void deleteComment(CommentEntity comment) {
        mAppExecutors.diskIO().execute(() -> mCommentDao.delete(comment));
    }

    public void deleteAllComments() {
        mAppExecutors.diskIO().execute(() -> mCommentDao.deleteAllComments());
    }

    public Integer getCommentsListCount() {
        Future<Integer> result = mAppExecutors.diskIO()
                .submit(() -> mCommentDao.getCommentsListCount());
        Integer returnValue;
        try {
            returnValue = result.get();
        } catch (Exception e) {
            return -1;
        }
        return returnValue;
    }

    public LiveData<PagedList<CommentEntity>> getCommentsFromPost(String postId, String commentsUrl) {
        int commentsAmount = getCommentsCount(postId);
        if (commentsAmount == 0) {
            loadComments(commentsUrl);
        }
        return getCommentsById(postId);
    }

    private void loadComments(String commentsUrl) {
        try {
            String[] splitUrl = commentsUrl.split(BASE_URL);
            commentsUrl = splitUrl[1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return;
        }

        String finalCommentsUrl = commentsUrl;
        mAppExecutors.networkIO().execute(() -> {
            Call<List<JsonCommentFeed>> call = mRedditApi.getCommentsList(finalCommentsUrl);
            mIsApiCommentsRefreshing.postValue(true);
            call.enqueue(new Callback<List<JsonCommentFeed>>() {
                @Override
                public void onResponse(Call<List<JsonCommentFeed>> call, Response<List<JsonCommentFeed>> response) {
                    List<CommentEntity> newComments = CommentEntity.createCommentsListFromEntries(response.body().get(1).getData().getEntries());
                    List<Long> result = insertAllComments(newComments);
                    mIsApiCommentsRefreshing.postValue(false);
                }

                @Override
                public void onFailure(Call<List<JsonCommentFeed>> call, Throwable t) {
                    mIsApiCommentsRefreshing.postValue(false);
                }
            });
        });
    }


    public void loadNextData(String subreddit, String lastPostId) {
        mAppExecutors.networkIO().execute(() -> {
            boolean isPopular = false;
            Call<JsonPostFeed> call = mRedditApi.getPosts(subreddit, PAGE_SIZE, lastPostId);
            if (subreddit.equals("popular")) {
                isPopular = true;
            }
            Boolean finalIsPopular = isPopular;
            mIsApiPostsRefreshing.postValue(true);
            call.enqueue(new Callback<JsonPostFeed>() {
                @Override
                public void onResponse(@NonNull Call<JsonPostFeed> call, @NonNull Response<JsonPostFeed> response) {

//                    if (response.body().getData() == null || response.body().getData().getEntries().size() == 0) {
//                        //TODO handle subreddit not found case
//                    }

                    List<PostEntity> newPosts = PostEntity.createPostsListFromEntries(response.body().getData().getEntries(), finalIsPopular);
                    List<Long> result = insertAllPosts(newPosts);
                    mIsApiPostsRefreshing.postValue(false);
                }

                @Override
                public void onFailure(@NonNull Call<JsonPostFeed> call, @NonNull Throwable t) {
                    mIsApiPostsRefreshing.postValue(false);
                }
            });
        });
    }

    public List<ISubreddit> getSubreddits() {
        return mSubreddits;
    }

}
