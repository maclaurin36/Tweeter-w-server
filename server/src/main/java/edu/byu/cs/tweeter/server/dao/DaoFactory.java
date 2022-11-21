package edu.byu.cs.tweeter.server.dao;

public interface DaoFactory {
    UserDao getUserDao();
    FollowDao getFollowDao();
    StatusDao getStatusDao();
    ImageDao getImageDao();
    FeedDao getFeedDao();
    StoryDao getStoryDao();
}
