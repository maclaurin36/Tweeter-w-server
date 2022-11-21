package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoAuthDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFeedDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStoryDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDao;
import edu.byu.cs.tweeter.server.dao.s3.S3ImageDao;

public class AwsDaoFactory implements DaoFactory {

    private final DynamoUserDao userDao;
    private final DynamoFollowDao followDao;
    private final S3ImageDao s3ImageDao;
    private final FeedDao feedDao;
    private final StoryDao storyDao;
    private final AuthDao authDao;

    public AwsDaoFactory() {
        userDao = new DynamoUserDao();
        followDao = new DynamoFollowDao();
        s3ImageDao = new S3ImageDao();
        feedDao = new DynamoFeedDao();
        storyDao = new DynamoStoryDao();
        authDao = new DynamoAuthDao();
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public FollowDao getFollowDao() {
        return followDao;
    }

    @Override
    public ImageDao getImageDao() { return s3ImageDao; }

    @Override
    public FeedDao getFeedDao() { return feedDao; }

    @Override
    public StoryDao getStoryDao() { return storyDao; }

    @Override
    public AuthDao getAuthDao() { return authDao; }
}
