package com.artem.nsu.redditfeed.model;

public interface IPost {

    String getId();

    String getTitle();

    String getAuthor();

    String getText();

    String getCreated();

    String getSubreddit();

    String getImage();

    String getUrl();

    Boolean isFavorite();

    void setFavorite(boolean isFavorite);

    String getComments();

    String getPostHint();

    String getScore();

    String getNumComments();

    Long getAddedTime();

    Boolean isPopular();

    String getVideoUrl();

}
