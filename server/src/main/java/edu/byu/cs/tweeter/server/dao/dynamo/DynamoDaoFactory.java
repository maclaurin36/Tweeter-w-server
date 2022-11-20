package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.StatusDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class DynamoDaoFactory implements DaoFactory {

    private final DynamoUserDao userDao;
    private final DynamoFollowDao followDao;
    private final DynamoStatusDao statusDao;

    public DynamoDaoFactory() {
        userDao = new DynamoUserDao();
        followDao = new DynamoFollowDao();
        statusDao = new DynamoStatusDao();
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
}
