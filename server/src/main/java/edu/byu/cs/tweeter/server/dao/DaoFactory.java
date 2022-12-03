package edu.byu.cs.tweeter.server.dao;

public interface DaoFactory {
    UserDao getUserDao();
    FollowDao getFollowDao();
    ImageDao getImageDao();
    FeedDao getFeedDao();
    StoryDao getStoryDao();
    AuthDao getAuthDao();
    QueueDao getQueueDao();
}
