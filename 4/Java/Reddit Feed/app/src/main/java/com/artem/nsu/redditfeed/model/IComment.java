package com.artem.nsu.redditfeed.model;

public interface IComment {

    String getId();

    String getAuthor();

    String getBody();

    String getCreated();

    String getScore();

    String getPostId();

}
