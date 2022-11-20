package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStatusDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDao;
import edu.byu.cs.tweeter.server.dao.s3.S3ImageDao;

public class AwsDaoFactory implements DaoFactory {

    private final DynamoUserDao userDao;
    private final DynamoFollowDao followDao;
    private final DynamoStatusDao statusDao;
    private final S3ImageDao s3ImageDao;

    public AwsDaoFactory() {
        userDao = new DynamoUserDao();
        followDao = new DynamoFollowDao();
        statusDao = new DynamoStatusDao();
        s3ImageDao = new S3ImageDao();
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
    public StatusDao getStatusDao() {
        return statusDao;
    }

    @Override
    public ImageDao getImageDao() { return s3ImageDao; }
}
